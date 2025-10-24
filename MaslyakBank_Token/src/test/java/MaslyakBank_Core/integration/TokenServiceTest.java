package MaslyakBank_Core.integration;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import details.CustomUserDetails;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import service.TokenService;
import dao.UserDAO;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TokenServiceTest {

    @Autowired private  UserDAO userDAO;
    @Autowired private  UserTokenDAO tokenDAO;
    @Autowired private  RefreshTokenDAO refreshTokenDAO;
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private TokenService tokenService;

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
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
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
        JwtTokenRequestDTO dto = new JwtTokenRequestDTO("testuser", "1234567890");

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
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);
        JwtTokenRequestDTO dto = new JwtTokenRequestDTO("testuser", "12345");

        // act + assert
        assertThatThrownBy(() -> tokenService.getAuthToken(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401 UNAUTHORIZED")
                .hasMessageContaining("Bad credentials");
    }

    @Test
    void refreshOrLogout_replaceRes() {
        // arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        // сразу сгенерим пару токенов, чтобы refresh уже существовал
        TokenPair tokens = tokenService.getToken("testuser");

        // act
        TokenPair newTokens = tokenService.refreshOrLogout(tokens.refreshToken());

        // assert
        assertNotNull(newTokens);
        assertNotNull(newTokens.accessToken());
        assertNotNull(newTokens.refreshToken());
    }




    @Test
    void refreshOrLogout_logoutOnly() {
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        // имитация успешной аутентификации
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // act
        TokenPair result = tokenService.refreshOrLogout(null);

        // assert
        assertNull(result);

        tokenDAO.findAllByUserId(user.getId()).forEach(t -> {
            assertTrue(t.isExpired());
            assertFalse(t.isValid());
        });

        SecurityContextHolder.clearContext();
    }
}
