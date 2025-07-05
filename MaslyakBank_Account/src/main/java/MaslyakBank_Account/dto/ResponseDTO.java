package MaslyakBank_Account.dto;

import MaslyakBank_Account.entity.AccountTable;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private AccountTable user;
}
