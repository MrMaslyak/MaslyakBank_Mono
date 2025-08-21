package system;

import entity.UserTokenTable;
import enums.TokenStatus;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
public class UserTokenBuilder {

    private final UserTokenTable userTokenTable = new UserTokenTable();

    public UserTokenBuilder withUser(UsersTable user) {
        userTokenTable.setUser(user);
        return this;
    }

    public UserTokenBuilder token(String token) {
        userTokenTable.setToken(token);
        userTokenTable.setCreatedAt(new Date());
        userTokenTable.setUpdatedAt(new Date());
        userTokenTable.setValid(true);
        userTokenTable.setExpired(false);
        userTokenTable.setStatus(TokenStatus.ACTIVE);
        return this;
    }

    public UserTokenTable build() {
        return userTokenTable;
    }
}
