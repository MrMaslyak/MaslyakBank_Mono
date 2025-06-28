package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.mappers.AccountMapper;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountDAO accountDAO;
    private final UserDAO userDAO;

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
        return accountDAO.saveAccount(account);
    }
}
