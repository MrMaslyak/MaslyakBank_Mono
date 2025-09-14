package MaslyakBank_Transaction.system.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class TransactionException extends ResponseStatusException {

    public TransactionException(HttpStatus status, String reason) {
        super(status, reason);
    }


}