package com.example.MaslyakBank_Token.unit;


import dao.RefreshTokenDAO;
import dao.UserDAO;
import dao.UserTokenDAO;
import dto.TokenPair;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import service.TokenService;
import system.JwtTokenGenerator;
import system.validators.RefreshTokenValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock private  RefreshTokenValidator refreshTokenValidator;
    @Mock private  AuthenticationManager authenticationManager;
    @Mock private  UserDAO userDAO;
    @Mock private  UserTokenDAO tokenDAO;
    @Mock private  RefreshTokenDAO refreshTokenDAO;
    @Mock private  JwtTokenGenerator tokenGenerator;

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
}
