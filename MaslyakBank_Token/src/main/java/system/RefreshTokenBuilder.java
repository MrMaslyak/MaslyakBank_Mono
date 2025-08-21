package system;

import entity.RefreshTokenTable;
import entity.UserTokenTable;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
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
        refreshTokenTable.setExpiredAt(LocalDateTime.now().plusDays(30));
        refreshTokenTable.setCreatedAt(new Date());
        refreshTokenTable.setUpdatedAt(new Date());
        return this;
    }

    public RefreshTokenTable build() {
        return refreshTokenTable;
    }
}
