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
        String iban = dto.getAccount_number();

        if (iban == null || iban.isBlank()) {
            return accountFactory.createAccount(userId, dto);
        }

        AccountTable account = accountDAO.findByIban(iban);
        if (account == null) {
            return accountFactory.createAccount(userId, dto);
        }

        if (!dto.getCurrency().equals(account.getCurrency())) {
            throw new IllegalArgumentException("In this account -> Currency mismatch");
        }

        return account;
    }


}
