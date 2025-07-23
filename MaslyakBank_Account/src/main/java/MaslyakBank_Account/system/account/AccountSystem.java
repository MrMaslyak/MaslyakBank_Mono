package MaslyakBank_Account.system.account;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountSystem {

    private final AccountDAO accountDAO;
    private final AccountFactory accountFactory;

    public AccountTable ensureAccount(UUID userId, CardRequestDTO dto) {
        if (dto.getAccount_number() == null || dto.getAccount_number().isBlank()) {
            return accountFactory.createAccount(userId, dto);
        }else {
            AccountTable account = accountDAO.findByIban(dto.getAccount_number());
            if (account == null) {
                return accountFactory.createAccount(userId, dto);
            }
            return account;
        }

    }


}
