package MaslyakBank_Transaction.service;


import MaslyakBank_Transaction.dao.DetailsDAO;
import MaslyakBank_Transaction.dao.TransactionDAO;
import MaslyakBank_Transaction.dto.CardValidationResultDTO;
import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.entity.TransactionDetailsTable;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.enums.TransactionDirectionType;
import MaslyakBank_Transaction.enums.TransactionStatus;
import MaslyakBank_Transaction.enums.TransactionType;
import MaslyakBank_Transaction.system.DetailsBuilder;
import MaslyakBank_Transaction.system.TransactionBuilder;
import MaslyakBank_Transaction.system.exception.TransactionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.AccountTable;
import entity.CardTable;
import enums.Currency;
import io.micrometer.core.aop.CountedAspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import util.SecurityUtil;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionBuilder transactionBuilder;
    private final DetailsBuilder detailsBuilder;
    private final TransactionDAO transactionDAO;
    private final DetailsDAO detailsDAO;
    private final RestClient cardManagmentService;
    private final RestClient accountManagmentService;
    private final RedisTemplate<String, TransactionTable> transactionRedisTemplate;
    private final RedisTemplate<String, TransactionDetailsTable> detailsRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public TransactionService(TransactionBuilder transactionBuilder, TransactionDAO transactionDAO, DetailsBuilder detailsBuilder, DetailsDAO detailsDAO,
                              @Qualifier("cardRestClient") RestClient cardManagmentService,
                              @Qualifier("accountRestClient") RestClient accountManagmentService,
                              RedisTemplate<String, TransactionTable> transactionRedisTemplate,
                              RedisTemplate<String, TransactionDetailsTable> detailsRedisTemplate) {
        this.transactionBuilder = transactionBuilder;
        this.transactionDAO = transactionDAO;
        this.detailsBuilder = detailsBuilder;
        this.detailsDAO = detailsDAO;
        this.cardManagmentService = cardManagmentService;
        this.accountManagmentService = accountManagmentService;
        this.transactionRedisTemplate = transactionRedisTemplate;
        this.detailsRedisTemplate = detailsRedisTemplate;
    }


    public void transferCardToCard(TransferDTO dto) {
        TransactionTable transaction = null;

        try {
            CardValidationResultDTO validation = checkCards(dto);

            transaction = saveTransactionRedis(
                    dto,
                    validation.getFromCard().getAccount().getCurrency(),
                    TransactionType.CardToCard
            );

            checkBalance(dto, transaction);

            transferOperation(dto);

            transaction.setStatus(TransactionStatus.SUCCESS);
            updateTransactionRedis(transaction);

            saveDetailsRedis(transaction,
                    validation.getFromCard().getAccount(),
                    validation.getFromCard(),
                    BigDecimal.valueOf(dto.getAmount()),
                    BigDecimal.valueOf(validation.getFromCard().getAccount().getBalance() - dto.getAmount()),
                    TransactionDirectionType.debit);

            saveDetailsRedis(transaction,
                    validation.getToCard().getAccount(),
                    validation.getToCard(),
                    BigDecimal.valueOf(dto.getAmount()),
                    BigDecimal.valueOf(validation.getToCard().getAccount().getBalance() + dto.getAmount()),
                    TransactionDirectionType.credit);

        } catch (TransactionException ex) {
            saveFailedTransaction(transaction, dto, ex.getMessage(), Currency.UAH);
            throw ex;
        } catch (Exception ex) {
            saveFailedTransaction(transaction, dto, "Unexpected error", Currency.UAH);
            throw new TransactionException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        }
    }

    @Scheduled(fixedRate = 60000) // куждую минуту
    public void schedulerRedisToDB() {
        Set<String> keysTransaction = transactionRedisTemplate.keys("transaction:*");
        Set<String> keysDetails = detailsRedisTemplate.keys("details:*");
        if (keysTransaction != null && keysDetails != null) {
            for (String key : keysTransaction) {
                TransactionTable transaction =  transactionRedisTemplate.opsForValue().get(key);
                if (transaction != null) {
                    transactionDAO.save(transaction);
                    transactionRedisTemplate.delete(key);
                }
            }
            for (String key : keysDetails) {
                TransactionDetailsTable details =  detailsRedisTemplate.opsForValue().get(key);
                if (details != null) {
                    detailsDAO.save(details);
                    detailsRedisTemplate.delete(key);
                }
            }
        }
    }


    private void transferOperation(TransferDTO dto){
        String token = SecurityUtil.getCurrentToken();
         accountManagmentService.post()
                .uri(uriBuilder -> uriBuilder.path("/transfer/card").build())
                .body(dto)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }

    private void updateTransactionRedis (TransactionTable transaction){
        transactionRedisTemplate.opsForValue().set("transaction:" + transaction.getId(), transaction);
    }


    private TransactionTable saveTransactionRedis(TransferDTO dto,Currency currency, TransactionType transactionType) {
        TransactionTable transaction = transactionBuilder
                .newTransaction()
                .transaction(UUID.randomUUID(), dto, currency, transactionType )
                .build();

        transactionRedisTemplate.opsForValue().set("transaction:" + transaction.getId(), transaction);
        return transaction;
    }

    private void saveDetailsRedis(TransactionTable transaction,
                                  AccountTable account,
                                  CardTable card,
                                  BigDecimal amount,
                                  BigDecimal balanceAfter,
                                  TransactionDirectionType transactionDirectionType) {

            TransactionDetailsTable details = detailsBuilder
                    .newDetails()
                    .details(transaction, account, card, amount, balanceAfter, transactionDirectionType)
                    .build();

            String key = "details:" + transaction.getId() + ":" + UUID.randomUUID();
            detailsRedisTemplate.opsForValue().set(key, details);

    }

    private void saveFailedTransaction(TransactionTable transaction, TransferDTO dto,
                                       String reason, Currency currency) {
        if (transaction == null) {
            transaction = transactionBuilder
                    .newTransaction()
                    .transaction(UUID.randomUUID(), dto, currency, TransactionType.CardToCard)
                    .build();
        }
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setFailedReason(reason);
        transactionDAO.save(transaction);
    }


    private CardValidationResultDTO checkCards(TransferDTO dto) {
        String token = SecurityUtil.getCurrentToken();
        try {
            return cardManagmentService.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("fromCardNumber", dto.getFromCardNumber())
                            .queryParam("toCardNumber", dto.getToCardNumber())
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(CardValidationResultDTO.class);
        } catch (RestClientResponseException ex) {
            String response = ex.getResponseBodyAsString();
            String message = response;
            try {
                JsonNode json = objectMapper.readTree(response);
                if (json.has("message")) {
                    message = json.get("message").asText();
                }
            } catch (Exception ignored) {
            }
            throw new TransactionException(
                    HttpStatus.valueOf(ex.getRawStatusCode()),
                    message
            );
        }
    }

    private void checkBalance(TransferDTO dto, TransactionTable transaction) {
        double fromBalance = getBalance(dto.getFromCardNumber());
        if (fromBalance < dto.getAmount()) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailedReason("Not enough money");
            transactionDAO.save(transaction);
            transactionRedisTemplate.delete("transaction:" + transaction.getId());
            throw new TransactionException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
    }

    public double getBalance(String cardNumber) {
        String token = SecurityUtil.getCurrentToken();
        return accountManagmentService.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/balance")
                        .queryParam("cardNumber", cardNumber)
                        .build()
                )
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Double.class);
    }

}
