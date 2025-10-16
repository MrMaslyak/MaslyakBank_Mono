package com.example.MaslyakBank_Token.unit;

import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import MaslyakBank_Core.service.user.UserService;
import dto.TokenPair;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;


import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock  private  RestClient tokenRestClient;
    @Mock  private  UserSecurityDAO userDAO;
    @Mock  private  UserMapper userMapper;
    @Mock  private  PasswordEncoder passwordEncoder;
    @Mock  private  RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser() {
        //arrange
        RegistrationRequestDTO dto = new RegistrationRequestDTO
                ( "test@example.com", "password123", "John", "Doe");
        UsersTable user = new UsersTable();
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");



        // act
        userService.registerUser(dto);

        //assert
        assertThat(user.getPasswordSalt()).isEqualTo("encodedPassword");
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getRole()).isEqualTo(UserRole.USER);

        verify(userMapper).toEntity(dto);
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userDAO).registrationUser(user);
    }

    @Test
    void requestRegistrationToken(){
        //arrange
        RegistrationRequestDTO dto = new RegistrationRequestDTO
                ( "test@example.com", "password123", "John", "Doe");
        UsersTable user = new UsersTable();
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        TokenPair mockTokenPair = new TokenPair("access", "refresh");

        RestClient.RequestBodyUriSpec bodySpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(tokenRestClient.post()).thenReturn(bodySpec);
        when(bodySpec.uri(anyString())).thenReturn(bodySpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TokenPair.class)).thenReturn(mockTokenPair);


        // act
        TokenPair result = userService.requestRegistrationToken(dto);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access");
        assertThat(result.refreshToken()).isEqualTo("refresh");

        verify(userMapper).toEntity(dto);
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userDAO).registrationUser(user);

        verify(tokenRestClient).post();
        verify(bodySpec).uri("/create");
        verify(bodySpec).body(dto.getLogin());
        verify(responseSpec).body(TokenPair.class);



    }
}
