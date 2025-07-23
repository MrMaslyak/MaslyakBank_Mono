package system;

import dao.UserDAO;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationUserStatus {

    private final UserDAO userDAO;


    public void checkStatus(UsersTable user) {
        user.setStatus(user.getStatus().next());
        userDAO.updateUser(user);
    }
}
