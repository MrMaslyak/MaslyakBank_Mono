package MaslyakBank_Token.integration;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.TokenPair;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import service.TokenService;
import dao.UserDAO;
import system.JwtTokenGenerator;
import system.validators.RefreshTokenValidator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TokenServiceTest {

    @Autowired private  RefreshTokenValidator refreshTokenValidator;
    @Autowired private  AuthenticationManager authenticationManager;
    @Autowired private  UserDAO userDAO;
    @Autowired private  UserTokenDAO tokenDAO;
    @Autowired private  RefreshTokenDAO refreshTokenDAO;
    @Autowired private  JwtTokenGenerator tokenGenerator;
    @Autowired private TokenService tokenService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanup() {
        refreshTokenDAO.deleteAll();
        tokenDAO.deleteAll();
        userDAO.deleteAll();
    }

    @Test
    void getToken() {
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPassword("testpass");
        userDAO.saveUser(user);

        //act
        TokenPair tokenPair = tokenService.getToken("testuser");

        //assert
        assertNotNull(tokenPair, "TokenPair не должен быть null");
        assertNotNull(tokenPair.accessToken(), "Access token не должен быть null");
        assertNotNull(tokenPair.refreshToken(), "Refresh token не должен быть null");

        System.out.println("Access: " + tokenPair.accessToken());
        System.out.println("Refresh: " + tokenPair.refreshToken());
    }

    @Test
    void getAuthToken_positive(){
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);
        JwtTokenRequestDTO dto = new JwtTokenRequestDTO(
                "testuser", "1234567890"
        );

        // act
        TokenPair tokenPair = tokenService.getAuthToken(dto);

        // assert
        assertNotNull(tokenPair, "TokenPair не должен быть null");
        assertNotNull(tokenPair.accessToken(), "Access токен не должен быть null");
        assertNotNull(tokenPair.refreshToken(), "Refresh токен не должен быть null");

        System.out.println("Access: " + tokenPair.accessToken());
        System.out.println("Refresh: " + tokenPair.refreshToken());

    }

    @Test
    void getAuthToken_negative(){
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        JwtTokenRequestDTO dto = new JwtTokenRequestDTO("testuser", "1234567890");

        // act + assert
        assertThatThrownBy(() -> tokenService.getAuthToken(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401 UNAUTHORIZED")
                .hasMessageContaining("Bad credentials");
    }
}
