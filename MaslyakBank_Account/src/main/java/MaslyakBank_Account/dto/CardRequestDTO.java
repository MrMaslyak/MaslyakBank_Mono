package MaslyakBank_Account.dto;


import MaslyakBank_Account.enums.CardType;
import enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDTO {

    private String account_number;
    private Currency currency;
    private CardType cardType;


}
