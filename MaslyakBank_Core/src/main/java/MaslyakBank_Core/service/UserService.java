package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserService {

    @Value("${admin}")
    private String adminSecretCode;
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
        return sendTokenRequest(dto.getLogin());
    }

    private void registerUser(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setPasswordSalt(encodePassword(dto.getPassword()));
        user.setStatus(UserStatus.REGISTERED);
        user.setRole(determineUserRole(dto));
        userDAO.registrationUser(user);
    }

    private String sendTokenRequest(String login) {
        return tokenRestClient.post()
                .uri("/registration/create")
                .body(login)
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

    private String encodePassword(String rawPassword){
       return passwordEncoder.encode(rawPassword);
    }

    private UserRole determineUserRole(RegistrationRequestDTO dto) {
        if (adminSecretCode.equals(dto.getSecretCode())) {
            return UserRole.ADMIN;
        } else if (dto.getSecretCode() == null || dto.getSecretCode().isBlank() ) {
            return UserRole.USER;
        }
        return null;
    }

}
