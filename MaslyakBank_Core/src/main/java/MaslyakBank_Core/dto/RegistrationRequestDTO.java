package MaslyakBank_Core.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequestDTO {

    private String login;
    private String email;
    private String password;
    private String passwordSalt;
    private String phoneNumber;


}
