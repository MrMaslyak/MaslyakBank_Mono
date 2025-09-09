package MaslyakBank_Account.system.builder;

import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.enums.BinCode;
import MaslyakBank_Account.system.CardSystem;
import entity.AccountTable;
import entity.CardTable;
import enums.CardType;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class CardBuilder {

    private CardTable card;


    public CardBuilder newCard() {
        this.card = new CardTable();
        return this;
    }

    public CardBuilder withAccount(AccountTable account) {
        card.setAccount(account);
        return this;
    }

    public CardBuilder card() {
        return card(null);
    }

    public CardBuilder card(@Nullable CardRequestDTO dto) {
        card.setCardNumber(CardSystem.generateCardNumber(BinCode.PRIVAT_MASTER.getValue()));
        card.setCvv(String.valueOf((int)(Math.random() * 900 + 100)));
        card.setExpiryDate(java.sql.Date.valueOf(LocalDate.now().plusYears(3)).toLocalDate());
        card.setCardType(dto != null && dto.getCardType() != null ? dto.getCardType() : CardType.DEBIT);
        card.set_expired(false);
        card.setBlocked(false);
        card.setCreatedAt(new Date());
        card.setUpdatedAt(new Date());
        return this;
    }


    public CardTable build() {
        return card;
    }
}
