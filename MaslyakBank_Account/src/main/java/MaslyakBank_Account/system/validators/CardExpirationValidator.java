package MaslyakBank_Account.system.validators;

import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.CardTable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class CardExpirationValidator implements CardValidator {


    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && fromCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new TransactionException(HttpStatus.BAD_REQUEST,"Source card is expired: " + fromCard.getCardNumber());
        }
        if (toCard != null && toCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new TransactionException(HttpStatus.BAD_REQUEST,"Destination card is expired: " + toCard.getCardNumber());
        }
    }
}
