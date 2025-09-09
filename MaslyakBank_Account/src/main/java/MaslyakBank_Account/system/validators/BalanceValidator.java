package MaslyakBank_Account.system.validators;


import MaslyakBank_Account.system.validators.exception.InsufficientFundsException;
import entity.CardTable;

public class BalanceValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && fromCard.getAccount().getBalance() < 0 ) {
            throw new InsufficientFundsException(fromCard.getCardNumber());
        }
    }
}