package system;

import entity.UsersTable;
import org.springframework.stereotype.Component;

@Component
public class VerificationUserStatus {

    public void checkStatus(UsersTable user) {
        user.setStatus(user.getStatus().next());
    }




}
