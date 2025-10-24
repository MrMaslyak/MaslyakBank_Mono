package MaslyakBank_Token.integration;
import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.mappers.UserMapper;
import MaslyakBank_Core.service.user.UserService;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired  private UserSecurityDAO userSecurityDAO;
    @Autowired  private UserDAO userDAO;
    @Autowired  private UserMapper userMapper;
    @Autowired  private PasswordEncoder passwordEncoder;
    @Autowired  private UserService userService;

    @Test
    void registerUser() {
        //arrange
        RegistrationRequestDTO dto = new RegistrationRequestDTO(
                "test",
                "test",
                "1234567890",
                "+3803032434"
        );

        //act
        userService.registerUser(dto);

        //assert
        UsersTable savedUser = userSecurityDAO.findByLogin("test");
        assertNotNull(savedUser, "Пользователь должен быть сохранён в базе");
        assertEquals(UserStatus.REGISTERED, savedUser.getStatus());
        assertEquals(UserRole.USER, savedUser.getRole());
        assertTrue(passwordEncoder.matches("1234567890", savedUser.getPasswordSalt()));

    }
}
