package MaslyakBank_Core.system;

import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;

@Component
public class SuperAdminInitializer {

    private final UserSecurityDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final RestClient tokenRestClient;


    public SuperAdminInitializer(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${superadmin.login}")
    private String login;
    @Value("${superadmin.password}")
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void initSuperAdmin() {
        if (!userDAO.existsByLogin(login)) {
            UsersTable superAdmin = new UsersTable();
            Date date = new Date();
            superAdmin.setLogin(login);
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setPhoneNumber("+3802342422342");
            superAdmin.setPasswordSalt(passwordEncoder.encode(password));
            superAdmin.setPassword(password);
            superAdmin.setStatus(UserStatus.COMPLETED);
            superAdmin.setRole(UserRole.SUPER_ADMIN);
            superAdmin.setCreatedAt(date);
            superAdmin.setUpdatedAt(date);
            userDAO.registrationUser(superAdmin);
            sendTokenRequest(login);
            System.out.println("SUPER_ADMIN created");
        }
    }

    private void sendTokenRequest(String login) {
        try {
            tokenRestClient.post()
                    .uri("/superadmin/create")
                    .body(login)
                    .retrieve()
                    .body(String.class);
            System.out.println("SUPER_ADMIN created and token requested");
        } catch (Exception e) {
            System.err.println("SUPER_ADMIN created, but failed to request token: " + e.getMessage());
        }
    }
}
