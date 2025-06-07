package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserDAO;
import MaslyakBank_Core.entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public UsersTable registration(String login, String email, String password, String phoneNumber) {
        UsersTable user = new UsersTable();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.setPasswordSalt("salt");
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userDAO.registrationUser(user);
    }
}
