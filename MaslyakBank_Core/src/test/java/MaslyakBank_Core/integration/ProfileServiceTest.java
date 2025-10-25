package MaslyakBank_Core.integration;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import MaslyakBank_Core.service.user.ProfileService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import util.SecurityUtil;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileServiceTest {

    @Autowired
    private ProfileDAO profileDAO;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private VerificationUserStatus verification;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProfileService profileService;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @Test
    void createProfile_success() {
        // Создаём пользователя
        UsersTable user = new UsersTable();
        user.setLogin("ivan");
        user.setPasswordSalt("encodedPass");
        user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        // Мокаем SecurityUtil
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            mocked.when(SecurityUtil::getCurrentToken).thenReturn("fake-jwt");

            // Настраиваем WireMock для пути account/create
            String path = "/maslyakbank/accountmanagment/account/create";
            wireMockExtension.stubFor(WireMock.post(WireMock.urlEqualTo(path))
                    .willReturn(WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{}")));

            ReflectionTestUtils.setField(profileService, "accountRestClient",
                    RestClient.builder()
                            .baseUrl(wireMockExtension.baseUrl() + "/maslyakbank/accountmanagment/account")
                            .build());
            // Создаём профиль
            ProfileRequestDTO dto = new ProfileRequestDTO(
                    "Иван", "Иванов", "Петрович",
                    "г. Киев", "ул. Леси Украинки", new Date()
            );

            ProfileTable profile = profileService.createProfile(dto);

            assertNotNull(profile);
            assertEquals(user.getId(), profile.getUser().getId());
        }
    }

    @Test
    void createProfile_accountCreationFails() {
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt("encodedPass");
        user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            mocked.when(SecurityUtil::getCurrentToken).thenReturn("fake-jwt");

            // Мокаем RestClient, чтобы выбрасывал исключение
            RestClient mockRestClient = org.mockito.Mockito.mock(RestClient.class);
            org.mockito.Mockito.when(mockRestClient.post())
                    .thenThrow(new RuntimeException("Сервис недоступен"));

            ReflectionTestUtils.setField(profileService, "accountRestClient", mockRestClient);

            ProfileRequestDTO dto = new ProfileRequestDTO(
                    "Иван", "Иванов", "Петрович",
                    "г. Киев", "ул. Леси Украинки", new Date()
            );

            org.assertj.core.api.Assertions.assertThatThrownBy(() -> profileService.createProfile(dto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Профиль создан, но не удалось создать счёт");
        }
    }
}
