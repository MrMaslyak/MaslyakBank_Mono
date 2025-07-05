package MaslyakBank_Token.dto;

import MaslyakBank_Token.entity.TokenTable;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private TokenTable tokenTable;
}
