package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.mappers.AccountMapper;
import MaslyakBank_Account.system.account.AccountSystem;
import MaslyakBank_Account.system.builder.AccountBuilder;
import dao.UserDAO;
import entity.UsersTable;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.VerificationUserStatus;
import util.SecurityUtil;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;
    private final UserDAO userDAO;
    private final CardService cardService;
    private final VerificationUserStatus  verification;
    private final AccountBuilder accountBuilder;


    @Transactional
    public AccountTable createAccount(AccountRequestDTO dto) {
        UsersTable user = SecurityUtil.getCurrentUser();
        verification.checkStatus(user);

        AccountTable account = accountBuilder
                .newAccount()
                .withUser(user)
                .defaultAccount(dto)
                .build();


        AccountTable savedAccount = accountDAO.saveAccount(account);
        createCard(savedAccount);
        userDAO.updateUser(user);

        return savedAccount;
    }

    private void createCard(AccountTable account) {
        CardTable cardDefault = cardService.createDefaultCard(account);
        cardDAO.createCard(cardDefault);
    }
}
