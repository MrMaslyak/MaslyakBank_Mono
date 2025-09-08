package MaslyakBank_Transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransferDTO {

    private String fromCardNumber;
    private String toCardNumber;
    private double amount;
    private String description;

}
