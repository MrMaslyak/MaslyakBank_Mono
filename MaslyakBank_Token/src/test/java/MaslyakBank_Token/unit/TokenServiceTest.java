package MaslyakBank_Token.unit;


import dao.RefreshTokenDAO;
import dao.UserDAO;
import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UserTokenTable;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import service.TokenService;
import system.JwtTokenGenerator;
import system.validators.RefreshTokenValidator;
import util.SecurityUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock private  UserDAO userDAO;
    @Mock private  RefreshTokenDAO refreshTokenDAO;
    @Mock private  UserTokenDAO tokenDAO;
    @Mock private  JwtTokenGenerator tokenGenerator;
    @Mock private  AuthenticationManager authenticationManager;
    @Mock private  RefreshTokenValidator refreshTokenValidator;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void getToken(){
        //arrange
        String login = "Test";
        UsersTable user = new UsersTable();
        TokenPair tokenPair = new TokenPair("access", "refresh");

        when(userDAO.findByLogin(login)).thenReturn(user);
        when(tokenGenerator.generateTokenPair(user)).thenReturn(tokenPair);

        //act
        TokenPair result = tokenService.getToken(login);

        //assert
        assertEquals("access", result.accessToken());
        assertEquals("refresh", result.refreshToken());
        verify(userDAO, times(1)).findByLogin(login);
        verify(tokenGenerator, times(1)).generateTokenPair(user);
    }


    @Test
    void getAuthToken_positive(){
        //arrange
        JwtTokenRequestDTO requestDTO = new JwtTokenRequestDTO("Test", "password123");
        TokenPair expectedPair = new TokenPair("access", "refresh");
        UsersTable user = new UsersTable();

        when(userDAO.findByLogin(requestDTO.getLogin())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(tokenDAO.findTokensByUser(user)).thenReturn(List.of());
        when(tokenGenerator.generateTokenPair(user)).thenReturn(expectedPair);

        //act
        TokenPair result = tokenService.getAuthToken(requestDTO);

        //assert
        assertEquals("access", result.accessToken());
        assertEquals("refresh", result.refreshToken());
        verify(userDAO, times(2)).findByLogin(requestDTO.getLogin());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenDAO, times(1)).findTokensByUser(user);
        verify(tokenGenerator, times(1)).generateTokenPair(user);

    }

    @Test
    void getAuthToken_negative(){
        //arrange
        JwtTokenRequestDTO requestDTO = new JwtTokenRequestDTO("Test", "password123");
        UsersTable user = new UsersTable();

        when(userDAO.findByLogin(requestDTO.getLogin())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // act + assert
        assertThatThrownBy(() -> tokenService.getAuthToken(requestDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401 UNAUTHORIZED")
                .hasMessageContaining("Bad credentials");

    }

    @Test
    void refreshOrLogoutToken(){
       //arrange
        String refreshToken = "refresh";
        UsersTable user = new UsersTable();
        UserTokenTable token = new UserTokenTable();
        token.setUser(user);
        RefreshTokenTable refreshTokenTable = new RefreshTokenTable();
        refreshTokenTable.setUserTokenTable(token);

        when(refreshTokenValidator.validate(refreshToken)).thenReturn(refreshTokenTable);
        when(tokenGenerator.generateTokenPair(user)).thenReturn(new TokenPair("access", "refresh"));

        //act
        TokenPair pair = tokenService.refreshOrLogout(refreshToken);

        //assert
        assertEquals("access", pair.accessToken());
        assertEquals("refresh", pair.refreshToken());
        verify(refreshTokenValidator, times(1)).validate(refreshToken);
        verify(tokenGenerator, times(1)).generateTokenPair(user);
        verify(tokenDAO).findAllByUserId(user.getId());
        verify(refreshTokenDAO).findAllByUserId(user.getId());
    }

    @Test
    void refreshOrLogout_returnsNull_whenRefreshTokenIsNull() {
        // arrange
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            UsersTable user = new UsersTable();
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(user);

            // act
            TokenPair result = tokenService.refreshOrLogout(null);

            // assert
            assertNull(result);
            verify(tokenDAO).findAllByUserId(user.getId());
        }
    }

    @Test
    void refreshOrLogout_returnsNull_whenRefreshTokenIsBlank() {
        // arrange
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            UsersTable user = new UsersTable();
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(user);

            // act
            TokenPair result = tokenService.refreshOrLogout("");

            // assert
            assertNull(result);
            verify(tokenDAO).findAllByUserId(user.getId());
        }
    }
}
