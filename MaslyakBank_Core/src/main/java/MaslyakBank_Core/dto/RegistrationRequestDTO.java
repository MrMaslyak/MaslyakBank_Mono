package MaslyakBank_Core.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RegistrationRequestDTO {

    private String login;
    private String email;
    private String password;
    private String phoneNumber;
}
