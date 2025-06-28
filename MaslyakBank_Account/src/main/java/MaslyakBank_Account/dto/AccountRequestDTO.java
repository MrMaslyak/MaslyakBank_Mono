package MaslyakBank_Account.dto;


import enums.AccountStatus;
import enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountRequestDTO {

    private UUID userId;

    private String accountNumber;

    private double balance;

    private Currency currency;

}
