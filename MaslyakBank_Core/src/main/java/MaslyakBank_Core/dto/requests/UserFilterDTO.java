package MaslyakBank_Core.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFilterDTO {

    private String role;
    private String status;


}
