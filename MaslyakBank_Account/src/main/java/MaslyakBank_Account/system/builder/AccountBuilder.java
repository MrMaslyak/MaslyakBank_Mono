package MaslyakBank_Account.system.builder;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.enums.AccountType;
import MaslyakBank_Account.system.account.IbanSystem;
import entity.UsersTable;
import enums.AccountStatus;
import enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccountBuilder {

    @Autowired
    private IbanSystem ibanSystem;
    private AccountTable account;

    public AccountBuilder newAccount() {
        this.account = new AccountTable();
        return this;
    }

    public AccountBuilder withUser(UsersTable user) {
        account.setUser(user);
        return this;
    }


    public AccountBuilder defaultAccount() {
        account.setCurrency(Currency.UAH);
        account.setAccountNumber(ibanSystem.generateIBAN());
        account.setStatus(AccountStatus.OPENED);
        account.setType(AccountType.CURRENT);
        account.setBalance(0.0);
        account.setBlocked(false);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());
        return this;
    }

    public AccountBuilder account(Currency currency) {
        account.setCurrency(currency);
        account.setAccountNumber(ibanSystem.generateIBAN());
        account.setStatus(AccountStatus.OPENED);
        account.setType(AccountType.CURRENT);
        account.setBalance(0.0);
        account.setBlocked(false);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());
        return this;
    }

    public AccountTable build() {
        return account;
    }

}
