package MaslyakBank_Core.integration;

import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.service.user.ProfileService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.Date;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import util.SecurityUtil;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProfileServiceTest {

    @Autowired private UserDAO userDAO;
    @Autowired private ProfileService profileService;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void createProfile_success() {
        //arrange
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

            ProfileRequestDTO dto = new ProfileRequestDTO(
                    "Иван", "Иванов", "Петрович",
                    "г. Киев", "ул. Леси Украинки", new Date()
            );

            //act
            ProfileTable profile = profileService.createProfile(dto);

            //assert
            assertNotNull(profile);
            assertEquals(user.getId(), profile.getUser().getId());
        }
    }

    @Test
    void createProfile_failed() {
        //arrange
        UsersTable user = new UsersTable();
        user.setLogin("testuser");
        user.setPasswordSalt("encodedPass");
        user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        user.setRole(UserRole.USER);
        userDAO.saveUser(user);

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            mocked.when(SecurityUtil::getCurrentToken).thenReturn("fake-jwt");

            wireMockExtension.stubFor(WireMock.post(WireMock.urlEqualTo("/maslyakbank/accountmanagment/account/create"))
                    .willReturn(WireMock.aResponse()
                            .withStatus(500)));

            ReflectionTestUtils.setField(profileService, "accountRestClient",
                    RestClient.builder()
                            .baseUrl(wireMockExtension.baseUrl() + "/maslyakbank/accountmanagment/account")
                            .build());

            ProfileRequestDTO dto = new ProfileRequestDTO(
                    "Иван", "Иванов", "Петрович",
                    "г. Киев", "ул. Леси Украинки", new Date()
            );


            //act+assert
          assertThatThrownBy(() -> profileService.createProfile(dto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Профиль создан, но не удалось создать счёт");
        }
    }
}
