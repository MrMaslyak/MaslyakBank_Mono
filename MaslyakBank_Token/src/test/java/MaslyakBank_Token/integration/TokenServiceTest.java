package MaslyakBank_Token.integration;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.TokenPair;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import service.TokenService;
import dao.UserDAO;
import system.JwtTokenGenerator;
import system.validators.RefreshTokenValidator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class TokenServiceTest {

    @Autowired private  RefreshTokenValidator refreshTokenValidator;
    @Autowired private  AuthenticationManager authenticationManager;
    @Autowired private  UserDAO userDAO;
    @Autowired private  UserTokenDAO tokenDAO;
    @Autowired private  RefreshTokenDAO refreshTokenDAO;
    @Autowired private  JwtTokenGenerator tokenGenerator;
    @Autowired private TokenService tokenService;



    @Test
    void getToken() {
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPassword("testpass");
        userDAO.updateUser(user);

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
    void getAuthToken(){

    }
}
