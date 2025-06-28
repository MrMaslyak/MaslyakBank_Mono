package MaslyakBank_Core.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDTO {

    private String login;
    private String password;
    private String email;
}
