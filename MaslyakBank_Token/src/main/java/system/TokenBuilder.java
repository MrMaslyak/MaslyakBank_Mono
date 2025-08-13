package system;

import entity.TokenTable;
import enums.TokenStatus;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class TokenBuilder {

    private final TokenTable tokenTable = new TokenTable();

    public TokenBuilder withUser(UsersTable user) {
        tokenTable.setUser(user);
        return this;
    }

    public TokenBuilder token(String token) {
        tokenTable.setToken(token);
        tokenTable.setCreatedAt(new Date());
        tokenTable.setUpdatedAt(new Date());
        tokenTable.setValid(true);
        tokenTable.setExpired(false);
        tokenTable.setStatus(TokenStatus.ACTIVE);
        return this;
    }

    public TokenTable build() {
        return tokenTable;
    }
}
