package MaslyakBank_Account.system.account;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.service.AccountService;
import MaslyakBank_Account.system.builder.AccountBuilder;
import dao.UserDAO;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountSystem {


    private final AccountDAO accountDAO;
    private final UserDAO userDAO;
    private final AccountBuilder accountBuilder;


    public AccountTable ensureAccount(UUID userId, CardRequestDTO dto) {
        AccountTable existing = accountDAO.findByCurrency(userId, dto.getCurrency());
        if (existing != null) {
            return existing;
        }
        return createAccount(userId, dto);
    }


    private AccountTable createAccount(UUID userId, CardRequestDTO dto){
        UsersTable user = userDAO.findById(userId);

        AccountTable account = accountBuilder
                .newAccount()
                .withUser(user)
                .account(dto.getCurrency())
                .build();

        return accountDAO.saveAccount(account);
    }
}
