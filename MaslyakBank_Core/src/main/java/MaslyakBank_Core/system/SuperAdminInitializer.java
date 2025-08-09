package MaslyakBank_Core.system;

import MaslyakBank_Core.dao.UserSecurityDAO;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
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
    private final RestClient tokenRestClient;
    private final PasswordEncoder passwordEncoder;


    public SuperAdminInitializer(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO, PasswordEncoder passwordEncoder) {
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
            userDAO.registrationUser(buildSuperAdmin(login,password));
            sendTokenRequest(login);
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

    private UsersTable buildSuperAdmin(String login, String password) {
        Date now = new Date();
        UsersTable user = new UsersTable();
        user.setLogin(login);
        user.setPassword(password);
        user.setPasswordSalt(passwordEncoder.encode(password));
        user.setEmail("superadmin@gmail.com");
        user.setPhoneNumber("+3802342422342");
        user.setStatus(UserStatus.COMPLETED);
        user.setRole(UserRole.SUPER_ADMIN);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        return user;
    }
}
