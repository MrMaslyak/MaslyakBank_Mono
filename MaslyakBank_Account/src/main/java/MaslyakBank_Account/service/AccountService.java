package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.mappers.AccountMapper;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserStatus;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.VerificationUserStatus;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;
    private final UserDAO userDAO;
    private final CardService cardService;
    private final VerificationUserStatus  verification;


    @Transactional
    public AccountTable createAccount(AccountRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        AccountTable account = accountMapper.toEntity(dto);
        account.setUser(user);


        verification.checkStatus(user);

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
