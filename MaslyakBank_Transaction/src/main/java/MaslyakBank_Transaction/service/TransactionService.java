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
import entity.AccountTable;
import entity.CardTable;
import enums.Currency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionBuilder transactionBuilder;
    private final DetailsBuilder detailsBuilder;
    private final TransactionDAO transactionDAO;
    private final DetailsDAO detailsDAO;
    private final RestClient cardManagmentService;
    private final RestClient accountManagmentService;
    private final RedisTemplate<String, Object> redisTemplate;


    public TransactionService(TransactionBuilder transactionBuilder, TransactionDAO transactionDAO, DetailsBuilder detailsBuilder, DetailsDAO detailsDAO,
                              @Qualifier("cardRestClient") RestClient cardManagmentService,
                              @Qualifier("accountRestClient") RestClient accountManagmentService, RedisTemplate<String, Object> redisTemplate) {
        this.transactionBuilder = transactionBuilder;
        this.transactionDAO = transactionDAO;
        this.detailsBuilder = detailsBuilder;
        this.detailsDAO = detailsDAO;
        this.cardManagmentService = cardManagmentService;
        this.accountManagmentService = accountManagmentService;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void transferCardToCard(TransferDTO dto) {
        TransactionTable transaction = saveTransactionRedis(dto,Currency.UAH, TransactionType.CardToCard);

        CardValidationResultDTO validation = checkCards(dto);
        if (validation == null) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.save(transaction);
            redisTemplate.delete("transaction:" + transaction.getId());
            throw new TransactionException(HttpStatus.BAD_REQUEST, "Cards are not valid");
        } else {
            transaction.setStatus(TransactionStatus.PENDING);
            transactionDAO.update(transaction);
        }

        transaction.setCurrency(validation.getFromCard().getAccount().getCurrency());
        updateTransactionRedis(transaction);

        if (!checkBalance(dto)) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.save(transaction);
            redisTemplate.delete("transaction:" + transaction.getId());
            throw new TransactionException(HttpStatus.BAD_REQUEST,"Not enough money");
        }

        saveDetails(transaction,
                validation.getFromCard().getAccount(),
                validation.getFromCard(),
                BigDecimal.valueOf(dto.getAmount()),
                BigDecimal.valueOf(validation.getFromCard().getAccount().getBalance() - dto.getAmount()),
                TransactionDirectionType.debit);

        saveDetails(transaction,
                validation.getToCard().getAccount(),
                validation.getToCard(),
                BigDecimal.valueOf(dto.getAmount()),
                BigDecimal.valueOf(validation.getToCard().getAccount().getBalance() + dto.getAmount()),
                TransactionDirectionType.credit);

        transferOperation(dto);

        TransactionTable finalTransaction = (TransactionTable) redisTemplate.opsForValue()
                .get("transaction:" + transaction.getId());

        transaction.setStatus(TransactionStatus.SUCCESS);

        transactionDAO.save(finalTransaction);
        redisTemplate.delete("transaction:" + transaction.getId());

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


    private TransactionTable saveTransactionRedis(TransferDTO dto,Currency currency, TransactionType transactionType) {
        TransactionTable transaction = transactionBuilder
                .newTransaction()
                .transaction(dto, currency, transactionType )
                .build();
        String redisKey = "transaction:" + UUID.randomUUID();
        redisTemplate.opsForValue().set(redisKey, transaction);
        return transaction;
    }

    private void updateTransactionRedis(TransactionTable transaction){
        redisTemplate.opsForValue().set("transaction:" + transaction.getId(), transaction);
    }

    private TransactionDetailsTable saveDetails(TransactionTable transaction, AccountTable account, CardTable card,BigDecimal amount, BigDecimal balanceAfter, TransactionDirectionType transactionDirectionType){
        TransactionDetailsTable details = detailsBuilder
                .newDetails()
                .details(transaction,account,card,amount, balanceAfter,transactionDirectionType)
                .build();
        detailsDAO.save(details);
        return details;
    }

    private CardValidationResultDTO checkCards(TransferDTO dto) {
        String token = SecurityUtil.getCurrentToken();
        return cardManagmentService.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/validate")
                        .queryParam("fromCardNumber", dto.getFromCardNumber())
                        .queryParam("toCardNumber", dto.getToCardNumber())
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(CardValidationResultDTO.class);
    }

    private boolean checkBalance(TransferDTO dto) {
        double fromBalance = getBalance(dto.getFromCardNumber());
        return fromBalance >= dto.getAmount();
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
