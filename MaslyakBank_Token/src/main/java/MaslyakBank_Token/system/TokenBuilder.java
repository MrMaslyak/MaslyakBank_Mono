package MaslyakBank_Token.system;


import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.enums.TokenRole;
import MaslyakBank_Token.enums.TokenStatus;
import entity.UsersTable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.function.Supplier;

@Component
public class TokenBuilder {

    private final TokenTable token = new TokenTable();

    public TokenBuilder withUser(UsersTable user) {
        token.setUser(user);
        return this;
    }

    public TokenBuilder withRole(TokenRole role) {
        token.setRole(role);
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

    public StringBuilder createRegistToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
            token.append(random.nextInt(10));
        }
        return token;
    }

    public TokenBuilder token(Supplier<StringBuilder> generator) {
        token.setToken(generator.get().toString());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setValid(true);
        token.setExpired(false);
        token.setStatus(TokenStatus.ACTIVE);
        return this;
    }

    public TokenTable build() {
        return token;
    }
}
