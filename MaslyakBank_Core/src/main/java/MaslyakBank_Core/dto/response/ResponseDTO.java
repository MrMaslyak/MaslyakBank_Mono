package MaslyakBank_Core.dto.response;

import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private UsersTable user;
}
