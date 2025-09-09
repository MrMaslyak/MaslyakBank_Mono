package MaslyakBank_Account.system.validators;

import MaslyakBank_Account.system.validators.exception.CardExpiredException;
import entity.CardTable;

import java.time.LocalDate;

public class CardExpirationValidator implements CardValidator {


    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && fromCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CardExpiredException("Source card is expired: " + fromCard.getCardNumber());
        }
        if (toCard != null && toCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CardExpiredException("Destination card is expired: " + toCard.getCardNumber());
        }
    }
}
