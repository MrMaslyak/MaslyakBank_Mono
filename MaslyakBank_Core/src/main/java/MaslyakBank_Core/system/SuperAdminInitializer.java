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
    private final PasswordEncoder passwordEncoder;


    public SuperAdminInitializer(UserSecurityDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${superadmin.login}")
    private String login;
    @Value("${superadmin.password}")
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void initSuperAdmin() {
        UsersTable existingUser = userDAO.findByLogin(login);

        if (existingUser == null) {
            userDAO.registrationUser(buildSuperAdmin(login, password));
            System.out.println("SUPER_ADMIN created");
        } else {
            boolean needsUpdate = false;

            if (!passwordEncoder.matches(password, existingUser.getPasswordSalt())) {
                existingUser.setPasswordSalt(passwordEncoder.encode(password));
                needsUpdate = true;
            }
            if (existingUser.getRole() != UserRole.SUPER_ADMIN) {
                existingUser.setRole(UserRole.SUPER_ADMIN);
                needsUpdate = true;
            }
            if (existingUser.getStatus() != UserStatus.COMPLETED) {
                existingUser.setStatus(UserStatus.COMPLETED);
                needsUpdate = true;
            }
            if (needsUpdate) {
                userDAO.updateUser(existingUser);
                System.out.println("SUPER_ADMIN updated");
            } else {
                System.out.println("SUPER_ADMIN already up-to-date");
            }
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
