package com.example.MaslyakBank_Token.unit;


import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UserTokenTable;
import entity.UsersTable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import system.JwtTokenGenerator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenGeneratorTest {

    @Mock private UserTokenDAO userTokenDAO;
    @Mock private RefreshTokenDAO refreshTokenDAO;

    @InjectMocks private JwtTokenGenerator tokenGenerator;

    @BeforeEach
    void init() {
        String secretKey = "MyTemporaryTestSecretKey1234567890";
        ReflectionTestUtils.setField(tokenGenerator, "secretKey", secretKey);
        tokenGenerator.init();
    }

    @Test
    void generateTokenPair(){
        //arrange
        UsersTable user = new UsersTable();
        user.setId(UUID.randomUUID());
        user.setLogin("TestUser");

        UserTokenTable savedAccess = new UserTokenTable();
        savedAccess.setId(UUID.randomUUID());
        when(userTokenDAO.saveToken(any(UserTokenTable.class))).thenReturn(savedAccess);

        //act
        TokenPair pair = tokenGenerator.generateTokenPair(user);

        //assert
        assertNotNull(pair);
        assertNotNull(pair.accessToken());
        assertNotNull(pair.refreshToken());
        assertTrue(pair.accessToken().length() > 10);
        assertTrue(pair.refreshToken().length() > 10);

        verify(userTokenDAO, times(1)).saveToken(any(UserTokenTable.class));
        verify(refreshTokenDAO, times(1)).saveToken(any(RefreshTokenTable.class));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenGenerator.secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(pair.accessToken())
                .getBody();

        assertEquals("TestUser", claims.getSubject());
        assertEquals(user.getId().toString(), claims.get("user_id").toString());
        assertTrue(claims.getExpiration().getTime() > System.currentTimeMillis());
    }
}
