package MaslyakBank_Token.system;

import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.enums.TokenStatus;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Supplier;

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
