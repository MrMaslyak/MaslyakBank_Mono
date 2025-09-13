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
import entity.AccountTable;
import entity.CardTable;
import enums.Currency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionBuilder transactionBuilder;
    private final DetailsBuilder detailsBuilder;
    private final TransactionDAO transactionDAO;
    private final DetailsDAO detailsDAO;
    private final RestClient cardManagmentService;
    private final RestClient accountManagmentService;


    public TransactionService(TransactionBuilder transactionBuilder, TransactionDAO transactionDAO, DetailsBuilder detailsBuilder, DetailsDAO detailsDAO,
                              @Qualifier("cardRestClient") RestClient cardManagmentService,
                              @Qualifier("accountRestClient") RestClient accountManagmentService) {
        this.transactionBuilder = transactionBuilder;
        this.transactionDAO = transactionDAO;
        this.detailsBuilder = detailsBuilder;
        this.detailsDAO = detailsDAO;
        this.cardManagmentService = cardManagmentService;
        this.accountManagmentService = accountManagmentService;
    }

    public void transferCardToCard(TransferDTO dto) {
       TransactionTable transaction = saveTransaction(dto, TransactionType.CardToCard);
        CardValidationResultDTO validation = checkCards(dto, transaction);
        if (validation == null) return;
        if (!checkBalance(dto, transaction)) return;

        saveDetails(transaction,
                validation.getFromCard().getAccount(),
                validation.getFromCard(),
                BigDecimal.valueOf(validation.getFromCard().getAccount().getBalance() - dto.getAmount()),
                TransactionDirectionType.DEBIT);

        saveDetails(transaction,
                validation.getToCard().getAccount(),
                validation.getToCard(),
                BigDecimal.valueOf(validation.getToCard().getAccount().getBalance() + dto.getAmount()),
                TransactionDirectionType.CREDIT);

        transferOperation(dto);

        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionDAO.update(transaction);

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


    private TransactionTable saveTransaction(TransferDTO dto, TransactionType transactionType) {
        TransactionTable transaction = transactionBuilder
                .newTransaction()
                .transaction(dto, Currency.UAH, transactionType )
                .build();

        transactionDAO.save(transaction);
        return transaction;
    }

    private TransactionDetailsTable saveDetails(TransactionTable transaction, AccountTable account, CardTable card, BigDecimal balanceAfter, TransactionDirectionType transactionDirectionType){
        TransactionDetailsTable details = detailsBuilder
                .newDetails()
                .details(transaction,account,card,balanceAfter,transactionDirectionType)
                .build();
        detailsDAO.save(details);
        return details;
    }

    private boolean checkBalance(TransferDTO dto, TransactionTable transaction) {
        double fromBalance = getBalance(dto.getFromCardNumber());

        if (fromBalance < dto.getAmount()) {
            System.out.println("Not enough money on the card, balance = " + fromBalance + ", need = " + dto.getAmount());
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.update(transaction);
            return false;
        }

        return true;
    }

    private CardValidationResultDTO checkCards(TransferDTO dto, TransactionTable transaction) {
        String token = SecurityUtil.getCurrentToken();
        try {
            CardValidationResultDTO result = cardManagmentService.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("fromCardNumber", dto.getFromCardNumber())
                            .queryParam("toCardNumber", dto.getToCardNumber())
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(CardValidationResultDTO.class);

            transaction.setStatus(result != null ? TransactionStatus.PENDING : TransactionStatus.FAILED);
            transactionDAO.update(transaction);
            return result;
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.update(transaction);
            return null;
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
