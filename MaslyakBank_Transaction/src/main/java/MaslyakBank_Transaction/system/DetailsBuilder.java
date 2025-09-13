package MaslyakBank_Transaction.system;


import MaslyakBank_Transaction.entity.TransactionDetailsTable;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.enums.TransactionDirectionType;
import entity.AccountTable;
import entity.CardTable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;


@Component
public class DetailsBuilder {

    private TransactionDetailsTable details;


    public DetailsBuilder newDetails() {
        this.details = new TransactionDetailsTable();
        return this;
    }


    public DetailsBuilder details(TransactionTable transaction, AccountTable account, CardTable card, BigDecimal balanceAfter, TransactionDirectionType transactionDirectionType) {
        details.setTransaction(transaction);
        details.setAccountTable(account);
        details.setCardTable(card);
        details.setBalanceAfter(balanceAfter);
        details.setDirectionType(transactionDirectionType);
        details.setOperationAt(new Date());
        details.setCreatedAt(new Date());
        details.setUpdatedAt(new Date());
        return this;
    }

    public TransactionDetailsTable build() {
        return details;
    }

}
