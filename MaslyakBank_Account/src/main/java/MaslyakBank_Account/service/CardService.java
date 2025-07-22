package MaslyakBank_Account.service;


import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.system.account.AccountSystem;
import MaslyakBank_Account.system.builder.CardBuilder;
import entity.UsersTable;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {

    private final CardBuilder cardBuilder;
    private final AccountSystem accountSystem;


    public CardTable createCard(UsersTable user, CardRequestDTO dto) {
        AccountTable account = accountSystem.ensureAccount(user.getId(), dto);
        return createCard(account, dto);
    }


    public CardTable createCard(AccountTable account, @Nullable CardRequestDTO dto) {
        return   cardBuilder
                .newCard()
                .withAccount(account)
                .card(dto)
                .build();
    }


}
