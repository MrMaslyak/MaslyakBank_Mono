package MaslyakBank_Account.system.validators.exception;

public class CardExpiredException extends RuntimeException{
    public CardExpiredException(String message) {
        super(message);
    }
}
