package MaslyakBank_Token.system;


import MaslyakBank_Token.entity.TokenTable;
import entity.UsersTable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class TokenBuilder {

    private final TokenTable token = new TokenTable();

    public TokenBuilder withUser(UsersTable user) {
        token.setUser(user);
        return this;
    }


    public StringBuilder createAuthToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            token.append(random.nextInt(20));
        }
        return token;
    }

    public TokenBuilder authToken() {
        token.setToken(createAuthToken().toString());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setValid(true);
        token.setExpired(false);
       return this;
    }

    public TokenTable build() {
        return token;
    }
}
