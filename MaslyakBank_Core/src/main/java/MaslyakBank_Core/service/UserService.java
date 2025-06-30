package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.LoginRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.dto.response.TokenResponseDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
@RequiredArgsConstructor
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

        tokenRestClient
                .post()
                .uri("/save")
                .body(user)
                .retrieve()
                .body(TokenResponseDTO.class);

        return new ResponseDTO("Login", true, user);
    }

}
