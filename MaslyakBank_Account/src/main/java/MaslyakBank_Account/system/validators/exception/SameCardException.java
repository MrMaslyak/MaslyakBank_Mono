package MaslyakBank_Account.system.validators.exception;

public class SameCardException extends RuntimeException{
    public SameCardException(String message) {
        super(message);
    }
}
