package MaslyakBank_Account.dto;


import enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AccountRequestDTO {

    private String accountNumber;

    private double balance;

    private Currency currency;

}
