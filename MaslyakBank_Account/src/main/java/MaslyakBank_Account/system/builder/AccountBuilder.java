package MaslyakBank_Account.system.builder;

import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.system.account.IbanSystem;
import entity.AccountTable;
import entity.UsersTable;
import enums.AccountStatus;
import enums.Currency;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
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


    public AccountBuilder account() {
        return account(null);
    }

    public AccountBuilder account(@Nullable CardRequestDTO dto) {
        account.setCurrency(dto != null && dto.getCurrency() != null ? dto.getCurrency() : Currency.UAH);
        account.setAccountNumber(ibanSystem.generateIBAN());
        account.setStatus(AccountStatus.OPENED);
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
