package MaslyakBank_Account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferDTO {

    private String fromCardNumber;
    private String toCardNumber;
    private double amount;
    private String description;

}
