package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
@AllArgsConstructor
public class UserService {

    private final UserSecurityDAO userDAO;
    private final UserMapper userMapper;
    private final RestClient tokenRestClient;



    public String requestRegistrationToken(RegistrationRequestDTO  dto) {
        UsersTable user = userMapper.toEntity(dto);
        userDAO.registrationUser(user);
        return tokenRestClient.post()
                .uri("/registration/create")
                .body(dto.getLogin())
                .retrieve()
                .body(String.class);
    }

    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUser(login);
    }

    public String requestAuthToken(JwtTokenRequestDTO  dto) {
        return tokenRestClient.post()
                .uri("/auth/create")
                .body(dto)
                .retrieve()
                .body(String.class);
    }

}
