package MaslyakBank_Account.system.validators;

import entity.CardTable;

public interface CardValidator {
    void validate(CardTable fromCard, CardTable toCard);
}