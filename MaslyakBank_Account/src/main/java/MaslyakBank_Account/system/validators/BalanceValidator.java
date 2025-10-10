package MaslyakBank_Account.system.validators;


import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.CardTable;
import org.springframework.http.HttpStatus;

public class BalanceValidator implements CardValidator {

    @Override
    public void validate(CardTable fromCard, CardTable toCard) {
        if (fromCard != null && fromCard.getAccount().getBalance() < 0 ) {
            throw new TransactionException(HttpStatus.BAD_REQUEST ,"Insufficient funds on card "+ fromCard.getCardNumber());
        }
    }
}