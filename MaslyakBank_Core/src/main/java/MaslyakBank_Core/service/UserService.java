package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.LoginRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.requests.TokenRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
@AllArgsConstructor
public class UserService {

    private final UserSecurityDAO userDAO;
    private final UserMapper userMapper;
    private final RestClient tokenRestClient;



    public ResponseDTO registration(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setStatus(UserStatus.REGISTERED);
        UsersTable savedUser = userDAO.registrationUser(user);
        return new ResponseDTO("Registration", true, savedUser);
    }

    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUser(login);
    }

    public ResponseDTO login(LoginRequestDTO dto) {
        UsersTable user = userDAO.login(dto);

        if (user == null) {
            return new ResponseDTO("User not found or credentials incorrect", false, null);
        }

        if (user.getStatus() != UserStatus.COMPLETED) {
            return new ResponseDTO("Registration is not completed", false, user);
        }

        sendToken(user);

        return new ResponseDTO("Login", true, user);
    }

    private void sendToken(UsersTable user) {
        TokenRequestDTO tokenDTO = new TokenRequestDTO(user.getId());
        tokenRestClient.post()
                .uri("/save")
                .body(tokenDTO)
                .retrieve()
                .toBodilessEntity();
    }
}
