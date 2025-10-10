package MaslyakBank_Transaction.system;

import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.enums.TransactionStatus;
import MaslyakBank_Transaction.enums.TransactionType;
import enums.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Component
public class TransactionBuilder {

    private TransactionTable transaction;


    public TransactionBuilder newTransaction() {
        this.transaction = new TransactionTable();
        return this;
    }


    public TransactionBuilder transaction(UUID id, TransferDTO dto, Currency currency, TransactionType transactionType) {
       transaction.setId(id);
        transaction.setCurrency(currency);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(transactionType);
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(BigDecimal.valueOf(dto.getAmount()));
        transaction.setOperationAt(new Date());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        return this;
    }

    public TransactionTable build() {
        return transaction;
    }

}
