package dto;

import entity.UserTokenTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private UserTokenTable userTokenTable;
}
