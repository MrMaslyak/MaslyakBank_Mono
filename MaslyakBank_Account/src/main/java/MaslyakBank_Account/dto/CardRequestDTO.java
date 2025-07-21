package MaslyakBank_Account.dto;


import MaslyakBank_Account.enums.CardType;
import enums.Currency;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDTO {

    private Currency currency;
    private CardType cardType;


}
