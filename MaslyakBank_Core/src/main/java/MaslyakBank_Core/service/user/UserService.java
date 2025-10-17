package MaslyakBank_Core.service.user;


import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import dto.TokenPair;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final RestClient tokenRestClient;
    private final UserSecurityDAO userDAO;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;


    public UserService(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, UserMapper userMapper, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    public TokenPair requestRegistrationToken(RegistrationRequestDTO dto) {
        registerUser(dto);
        return sendTokenRequest(dto.getLogin(), "/create");
    }

    public void registerUser(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setPasswordSalt(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.REGISTERED);
        user.setRole(UserRole.USER);
        userDAO.registrationUser(user);
    }


    public TokenPair sendAuthRequest(JwtTokenRequestDTO dto) {
        return tokenRestClient.post()
                .uri("/auth/create")
                .body(dto)
                .retrieve()
                .body(TokenPair.class);
    }

    public void sendLogoutRequest() {
        String token = SecurityUtil.getCurrentToken();
        String login = SecurityUtil.getCurrentUser().getLogin();
        redisTemplate.opsForValue().set(
                    "token: " + token,
                    login,
                    5 * 60 * 1000, //5 минут храниться токен в блек листе
                    TimeUnit.MILLISECONDS
            );
        tokenRestClient.post()
                .uri("/logout")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }

    private TokenPair sendTokenRequest(String login, String uri) {
        return tokenRestClient.post()
                .uri(uri)
                .body(login)
                .retrieve()
                .body(TokenPair.class);
    }
}