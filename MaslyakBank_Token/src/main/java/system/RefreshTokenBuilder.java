package system;

import entity.RefreshTokenTable;
import entity.UserTokenTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@AllArgsConstructor
public class RefreshTokenBuilder {

    private final RefreshTokenTable refreshTokenTable = new RefreshTokenTable();

    public RefreshTokenBuilder withUserToken(UserTokenTable userTokenTable) {
        refreshTokenTable.setUserTokenTable(userTokenTable);
        return this;
    }

    public RefreshTokenBuilder token(String token) {
        refreshTokenTable.setToken(token);
        refreshTokenTable.setRevoked(false);
        refreshTokenTable.setExpired(false);
        refreshTokenTable.setCreatedAt(new Date());
        refreshTokenTable.setUpdatedAt(new Date());
        return this;
    }

    public RefreshTokenTable build() {
        return refreshTokenTable;
    }
}
