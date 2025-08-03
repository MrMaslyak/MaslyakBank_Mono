package MaslyakBank_Core.dto.requests;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequestDTO {

    private String login;
    private String email;
    private String password;
    private String phoneNumber;
    private String secretCode;


}
