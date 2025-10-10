package MaslyakBank_Account.system.validators.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Getter
public class TransactionException extends RuntimeException {
    private final HttpStatus status;

    public TransactionException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}