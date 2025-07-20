package MaslyakBank_Account.dto;


import enums.Currency;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    private Currency currency;

}
