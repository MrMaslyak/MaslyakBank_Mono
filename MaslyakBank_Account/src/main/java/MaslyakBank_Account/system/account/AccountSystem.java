package MaslyakBank_Account.system.account;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountSystem {

    private final AccountDAO accountDAO;

    public  boolean isExistAccount(UsersTable user, AccountRequestDTO dto){
        AccountTable existingAccount = accountDAO.findByCurrency(user.getId(), dto.getCurrency());
        if (existingAccount != null) {
            return true;
        }
        // Если нет — создаём новый счёт //todo
        return false;
    }
}
