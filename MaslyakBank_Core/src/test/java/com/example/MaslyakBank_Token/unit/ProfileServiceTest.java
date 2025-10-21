package com.example.MaslyakBank_Token.unit;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import MaslyakBank_Core.service.user.ProfileService;
import dao.UserDAO;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;
import util.SecurityUtil;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock private ProfileDAO profileDAO;
    @Mock private ProfileMapper profileMapper;
    @Mock private VerificationUserStatus verification;
    @Mock private RestClient accountRestClient;
    @Mock private UserDAO userDAO;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void createProfile_success() {
        // arrange
        ProfileRequestDTO dto = new ProfileRequestDTO(
                "test",
                "test",
                "test",
                "test",
                "test",
                new Date()
        );

        UsersTable user = new UsersTable();
        user.setId(java.util.UUID.randomUUID());

        ProfileTable mappedProfile = new ProfileTable();
        ProfileTable savedProfile = new ProfileTable();

        // Мокаем маппер и DAO
        when(profileMapper.toEntity(dto)).thenReturn(mappedProfile);
        when(profileDAO.saveProfile(mappedProfile)).thenReturn(savedProfile);

        // Мокаем RestClient цепочку
        RestClient.RequestBodyUriSpec mockRequest = mock(RestClient.RequestBodyUriSpec.class);
        when(accountRestClient.post()).thenReturn(mockRequest);
        when(mockRequest.uri("/create")).thenReturn(mockRequest);
        when(mockRequest.header(any(), any())).thenReturn(mockRequest);
        when(mockRequest.retrieve()).thenReturn(mock(RestClient.ResponseSpec.class));

        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(user);
            mockedSecurityUtil.when(SecurityUtil::getCurrentToken).thenReturn("fake-token");

            doNothing().when(verification).checkStatus(user);
            doNothing().when(userDAO).updateUser(any());

            // act
            ProfileTable result = profileService.createProfile(dto);

            // assert
            assertThat(result).isEqualTo(savedProfile);
            verify(verification).checkStatus(user);
            verify(profileMapper).toEntity(dto);
            verify(profileDAO).saveProfile(mappedProfile);
            verify(userDAO).updateUser(user);
            verify(accountRestClient).post();
        }
    }
}
