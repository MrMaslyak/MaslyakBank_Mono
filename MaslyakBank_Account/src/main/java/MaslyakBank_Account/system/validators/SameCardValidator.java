package MaslyakBank_Account.system.validators;


import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.CardTable;
import org.springframework.http.HttpStatus;

public class SameCardValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && toCard != null &&
                fromCard.getCardNumber().equals(toCard.getCardNumber())) {
            throw new TransactionException(HttpStatus.CONFLICT,"From and To card its same card");
        }
    }
}
