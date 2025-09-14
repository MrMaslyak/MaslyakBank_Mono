package MaslyakBank_Transaction.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class TransactionException extends RuntimeException {
    private final HttpStatus status;

    public TransactionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}