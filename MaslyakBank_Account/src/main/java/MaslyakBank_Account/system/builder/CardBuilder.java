package MaslyakBank_Account.system.builder;

import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.enums.BinCode;
import MaslyakBank_Account.enums.CardType;
import MaslyakBank_Account.system.CardSystem;
import enums.Currency;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class CardBuilder {

    private  CardTable card;


    public CardBuilder newCard() {
        this.card = new CardTable();
        return this;
    }

    public CardBuilder withAccount(AccountTable account) {
        card.setAccount(account);
        return this;
    }

    public CardBuilder defaultCard() {
        card.setCardNumber(CardSystem.generateCardNumber(BinCode.PRIVAT_MASTER.getValue()));
        card.setCvv(String.valueOf((int)(Math.random() * 900 + 100)));
        card.setExpiryDate(java.sql.Date.valueOf(LocalDate.now().plusYears(3)));
        card.setCardType(CardType.DEBIT);
        card.set_expired(false);
        card.setBlocked(false);
        card.setCreatedAt(new Date());
        card.setUpdatedAt(new Date());
        return this;
    }

    public CardBuilder card(CardRequestDTO dto) {
        card.setCardNumber(CardSystem.generateCardNumber(BinCode.PRIVAT_MASTER.getValue()));
        card.setCvv(String.valueOf((int)(Math.random() * 900 + 100)));
        card.setExpiryDate(java.sql.Date.valueOf(LocalDate.now().plusYears(3)));
        card.setCardType(dto.getCardType());
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
