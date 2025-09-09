package MaslyakBank_Transaction.service;


import MaslyakBank_Transaction.config.RestClientConfig;
import MaslyakBank_Transaction.dao.TransactionDAO;
import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.enums.TransactionStatus;
import MaslyakBank_Transaction.enums.TransactionType;
import MaslyakBank_Transaction.system.TransactionBuilder;
import enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

@Service
public class TransactionService {

    private final TransactionBuilder transactionBuilder;
    private final TransactionDAO transactionDAO;
    private final RestClient cardManagmentService;
    private final RestClient accountManagmentService;


    public TransactionService(TransactionBuilder transactionBuilder, TransactionDAO transactionDAO,
                              @Qualifier("cardRestClient") RestClient cardManagmentService,
                              @Qualifier("accountRestClient") RestClient accountManagmentService) {
        this.transactionBuilder = transactionBuilder;
        this.transactionDAO = transactionDAO;
        this.cardManagmentService = cardManagmentService;
        this.accountManagmentService = accountManagmentService;
    }

    public void transferCardToCard(TransferDTO dto) {
       TransactionTable transaction = saveTransaction(dto, TransactionType.CardToCard);
       checkCards(dto, transaction);

    }


    private TransactionTable saveTransaction(TransferDTO dto, TransactionType transactionType) {
        TransactionTable transaction = transactionBuilder
                .newTransaction()
                .transaction(dto, Currency.UAH, transactionType )
                .build();

        transactionDAO.save(transaction);
        return transaction;
    }

    private void checkCards(TransferDTO dto, TransactionTable transaction) {
        String token = SecurityUtil.getCurrentToken();
        try {
            System.out.println("Requesting CardService...");
            Boolean result = cardManagmentService.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("fromCardNumber", dto.getFromCardNumber())
                            .queryParam("toCardNumber", dto.getToCardNumber())
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(Boolean.class);

            transaction.setStatus(Boolean.TRUE.equals(result) ? TransactionStatus.PENDING : TransactionStatus.FAILED);
        } catch (Exception e) {
            System.out.println("Card validation failed: " + e.getMessage());
            transaction.setStatus(TransactionStatus.FAILED);
        } finally {
            transactionDAO.update(transaction);
        }
    }

}
