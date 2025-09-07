package MaslyakBank_Transaction.dto;

import enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransferCardToCardDTO {

    private UUID fromCardNumber;
    private UUID toCardNumber;
    private double amount;
    private String description;

}
