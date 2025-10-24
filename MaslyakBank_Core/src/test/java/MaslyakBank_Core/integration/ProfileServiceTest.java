package MaslyakBank_Core.integration;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import MaslyakBank_Core.service.user.ProfileService;
import com.github.tomakehurst.wiremock.client.WireMock;
import dao.UserDAO;
import details.CustomUserDetails;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import com.github.tomakehurst.wiremock.WireMockServer;
import util.SecurityUtil;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProfileServiceTest {

    @Autowired  private ProfileDAO profileDAO;
    @Autowired  private ProfileMapper profileMapper;
    @Autowired  private VerificationUserStatus verification;
    @Autowired  private PasswordEncoder passwordEncoder;
    @Autowired  private  UserDAO userDAO;

    @Autowired private ProfileService profileService;

    private WireMockServer wireMockServer;


    @Test
    void createProfile_success() {
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        // Auth user in SecurityContext (для безопасности)
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Мокаем SecurityUtil.getCurrentUser()
        try (MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {
            mockedSecurity.when(SecurityUtil::getCurrentUser).thenReturn(user);

            ProfileRequestDTO dto = new ProfileRequestDTO(
                    "Иван",
                    "Иванов",
                    "Петрович",
                    "г. Киев",
                    "ул. Ленина",
                    new Date()
            );

            // WireMock stub
            wireMockServer.stubFor(WireMock.post("/create")
                    .willReturn(WireMock.aResponse().withStatus(200)));

            // act
            ProfileTable createdProfile = profileService.createProfile(dto);

            // assert
            assertNotNull(createdProfile);
            assertEquals(user.getId(), createdProfile.getUser().getId());
        }
    }


    @Test
    void createProfile_accountCreationFails() {
        // arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt(passwordEncoder.encode("1234567890"));
        user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        // Auth user in System
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ProfileRequestDTO dto = new ProfileRequestDTO(
                "Иван",
                "Иванов",
                "Петрович",
                "г. Киев",
                "ул. Ленина",
                new Date()
        );

        // Мокаем accountRestClient, чтобы он бросал исключение
        RestClient mockRestClient = Mockito.mock(RestClient.class);
        Mockito.when(mockRestClient.post())
                .thenThrow(new RuntimeException("Сервис недоступен"));


        // Внедряем мок в профиль сервис через рефлексию или конструктор
        ReflectionTestUtils.setField(profileService, "accountRestClient", mockRestClient);

        // act + assert
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> profileService.createProfile(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Профиль создан, но не удалось создать счёт");
    }
}
