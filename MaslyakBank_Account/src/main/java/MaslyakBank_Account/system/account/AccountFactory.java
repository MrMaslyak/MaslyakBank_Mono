package MaslyakBank_Account.system.account;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.system.builder.AccountBuilder;
import dao.UserDAO;
import entity.AccountTable;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final AccountBuilder accountBuilder;


    public AccountTable createAccount(UUID userId, CardRequestDTO dto){
        UsersTable user = userDAO.findById(userId);

        AccountTable account = accountBuilder
                .newAccount()
                .withUser(user)
                .account(dto)
                .build();

        return accountDAO.saveAccount(account);
    }

    public AccountTable createAccountFor(UsersTable user) {
        AccountTable account = accountBuilder
                .newAccount()
                .withUser(user)
                .account()
                .build();

        return accountDAO.saveAccount(account);
    }

}
