package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserService {

    private final RestClient tokenRestClient;
    private final UserSecurityDAO userDAO;
    private final UserMapper userMapper;

    public UserService(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, UserMapper userMapper) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    public String requestRegistrationToken(RegistrationRequestDTO  dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setStatus(UserStatus.REGISTERED);
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
