package MaslyakBank_Account.dto;

import entity.AccountTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private AccountTable user;
}
