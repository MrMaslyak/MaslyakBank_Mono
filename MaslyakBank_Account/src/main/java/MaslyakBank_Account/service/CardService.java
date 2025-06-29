package MaslyakBank_Account.service;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.enums.BinCode;
import MaslyakBank_Account.enums.CardType;
import MaslyakBank_Account.system.CardNumberGeneration;
import enums.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@AllArgsConstructor
public class CardService {

    public CardTable createDefaultCardForAccount(AccountTable account) {
        CardTable card = new CardTable();
        card.setAccount(account);
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpiryDate(generateExpiryDate());
        card.setCardType(CardType.DEBIT);
        card.setCurrency(Currency.UAH);

        card.set_expired(false);
        card.setBlocked(false);
        card.setCreatedAt(new Date());
        card.setUpdatedAt(new Date());
        return card;
    }

    private String generateCardNumber() {
        return CardNumberGeneration.generateCardNumber(BinCode.PRIVAT_MASTER.getValue());
    }


    private String generateCVV() {
        return String.valueOf((int)(Math.random() * 900 + 100));
    }

    private Date generateExpiryDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusYears(3);
        return java.sql.Date.valueOf(expiryDate);
    }
}
