package MaslyakBank_Account.dto;

import entity.CardTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardValidationResultDTO {

    private CardTable fromCard;
    private CardTable toCard;

}
