package MaslyakBank_Core.service.user;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserService {

    private final RestClient tokenRestClient;
    private final UserSecurityDAO userDAO;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String requestRegistrationToken(RegistrationRequestDTO dto) {
        registerUser(dto);
        return sendTokenRequest(dto.getLogin(), "/create");
    }

    private void registerUser(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setPasswordSalt(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.REGISTERED);
        user.setRole(UserRole.USER);
        userDAO.registrationUser(user);
    }

    public String sendAuthRequest(JwtTokenRequestDTO dto) {
        return tokenRestClient.post()
                .uri("/auth/create")
                .body(dto)
                .retrieve()
                .body(String.class);
    }

    private String sendTokenRequest(String login, String uri) {
        return tokenRestClient.post()
                .uri(uri)
                .body(login)
                .retrieve()
                .body(String.class);
    }
}