package MaslyakBank_Core.integration;
import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.service.user.UserService;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MaslyakBank_Core.MaslyakBankCoreApplication.class)
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserSecurityDAO userSecurityDAO;
    @Autowired private PasswordEncoder passwordEncoder;


    @Test
    void registerUser_shouldSaveUserInDatabase() {
        // arrange
        RegistrationRequestDTO dto = new RegistrationRequestDTO(
                "test",
                "test",
                "1234567890",
                "+3803032434"
        );

        // act
        userService.registerUser(dto);

        // assert
        UsersTable savedUser = userSecurityDAO.findByLogin("test");
        assertNotNull(savedUser);
        assertEquals(UserStatus.REGISTERED, savedUser.getStatus());
        assertEquals(UserRole.USER, savedUser.getRole());
        assertTrue(passwordEncoder.matches("1234567890", savedUser.getPasswordSalt()));
    }
}
