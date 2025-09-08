package MaslyakBank_Transaction.service;


import MaslyakBank_Transaction.dao.TransactionDAO;
import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.enums.TransactionType;
import MaslyakBank_Transaction.system.TransactionBuilder;
import enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionBuilder transactionBuilder;
    private final TransactionDAO transactionDAO;

    public void transferCardToCard(TransferDTO dto) {
        saveTransaction(dto, TransactionType.CardToCard);
    }


    private TransactionTable saveTransaction(TransferDTO dto, TransactionType transactionType) {
        TransactionTable transaction = transactionBuilder
                .newTransaction()
                .transaction(dto, Currency.EUR, transactionType )
                .build();

        transactionDAO.save(transaction);
        return transaction;
    }

}
