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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;
    private final UserDAO userDAO;
    private final CardService cardService;


    @Transactional
    public AccountTable createAccount(AccountRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        AccountTable account = accountMapper.toEntity(dto);
        account.setUser(user);
        if (user.getStatus() == UserStatus.PARTIALLY_COMPLETED){
            user.setStatus(UserStatus.COMPLETED);
        }else {
            user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        }
        userDAO.updateUser(user);

        AccountTable savedAccount = accountDAO.saveAccount(account);
        createCard(savedAccount);

        return savedAccount;
        //restTemplate между двумя сервисами
    }

    private void createCard(AccountTable account) {
        CardTable cardDefault = cardService.createDefaultCardForAccount(account);
        cardDAO.createCard(cardDefault);
    }
}
