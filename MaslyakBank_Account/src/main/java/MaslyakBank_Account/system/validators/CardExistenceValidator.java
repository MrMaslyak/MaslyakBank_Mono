package MaslyakBank_Account.system.validators;

import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.CardTable;
import org.springframework.http.HttpStatus;

public class CardExistenceValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard == null) {
            throw new TransactionException(HttpStatus.NOT_FOUND,"Source card not found");
        }
        if (toCard == null) {
            throw new TransactionException(HttpStatus.NOT_FOUND,"Destination card not found");
        }
    }
}
