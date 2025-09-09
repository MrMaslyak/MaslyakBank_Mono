package MaslyakBank_Account.system.validators.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super("Don`t hane enought money: " + message);
    }
}
