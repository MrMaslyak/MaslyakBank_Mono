package dto;

import entity.TokenTable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private boolean success;
    private TokenTable tokenTable;
}
