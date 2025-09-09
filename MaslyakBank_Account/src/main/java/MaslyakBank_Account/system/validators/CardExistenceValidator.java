package MaslyakBank_Account.system.validators;

import MaslyakBank_Account.system.validators.exception.CardNotFoundException;
import entity.CardTable;

public class CardExistenceValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard == null) {
            throw new CardNotFoundException("Source card not found");
        }
        if (toCard == null) {
            throw new CardNotFoundException("Destination card not found");
        }
    }
}
