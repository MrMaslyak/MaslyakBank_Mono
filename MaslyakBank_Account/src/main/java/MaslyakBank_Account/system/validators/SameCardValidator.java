package MaslyakBank_Account.system.validators;


import MaslyakBank_Account.system.validators.exception.SameCardException;
import entity.CardTable;

public class SameCardValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && toCard != null &&
                fromCard.getCardNumber().equals(toCard.getCardNumber())) {
            throw new SameCardException("From and To card its same card");
        }
    }
}
