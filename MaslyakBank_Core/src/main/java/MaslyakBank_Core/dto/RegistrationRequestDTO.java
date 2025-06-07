package MaslyakBank_Core.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.Date;

@Data
@AllArgsConstructor
public class RegistrationRequestDTO {

    private String login;
    private String email;
    private String password;
    private String phoneNumber;

}
