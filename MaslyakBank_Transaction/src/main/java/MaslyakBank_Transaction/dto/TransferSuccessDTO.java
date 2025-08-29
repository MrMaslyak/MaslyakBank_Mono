package MaslyakBank_Transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferSuccessDTO {
    private String message;
    private boolean success;
}
