package com.example.MaslyakBank_Token.unit;


import dao.UserDAO;
import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.TokenPair;
import entity.UserTokenTable;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import service.TokenService;
import system.JwtTokenGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock private  UserDAO userDAO;
    @Mock private  JwtTokenGenerator tokenGenerator;
    @Mock private  AuthenticationManager authenticationManager;
    @Mock private  UserTokenDAO tokenDAO;

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
    void getAuthToken(){
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

}
