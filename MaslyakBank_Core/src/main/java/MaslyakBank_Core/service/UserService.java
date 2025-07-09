package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
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

        return null;
    }

    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUser(login);
    }

    public String requestToken(JwtTokenRequestDTO  dto) {
        return tokenRestClient.post()
                .uri("/create")
                .body(dto)
                .retrieve()
                .body(String.class);
    }

}
