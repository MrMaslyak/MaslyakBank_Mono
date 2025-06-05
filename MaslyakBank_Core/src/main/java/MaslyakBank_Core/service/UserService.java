package MaslyakBank_Core.service;


import MaslyakBank_Core.dto.RegistrationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    public RegistrationRequestDTO registration(String login, String email, String password, String phoneNumber) {
        return new RegistrationRequestDTO(login, email, password, phoneNumber);
    }
}
