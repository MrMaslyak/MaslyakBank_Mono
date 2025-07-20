package MaslyakBank_Account.service;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.system.builder.CardBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {

    private final CardBuilder cardBuilder;

    public CardTable createDefaultCard(AccountTable account) {
        return   cardBuilder
                .newCard()
                .withAccount(account)
                .defaultCard()
                .build();
    }

}
