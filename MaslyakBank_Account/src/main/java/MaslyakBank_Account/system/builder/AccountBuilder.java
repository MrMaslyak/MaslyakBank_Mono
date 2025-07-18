package MaslyakBank_Account.system.builder;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.enums.AccountType;
import MaslyakBank_Account.system.account.AccountNumberGeneration;
import entity.UsersTable;
import enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccountBuilder {

    private final AccountTable account;
    private AccountNumberGeneration accountNG;

    public AccountBuilder withUser(UsersTable user) {
        account.setUser(user);
        return this;
    }


    public AccountBuilder DefaultAccount() {
        account.setAccountNumber(accountNG.generateNumber());
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
