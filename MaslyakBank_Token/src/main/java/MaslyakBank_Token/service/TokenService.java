package MaslyakBank_Token.service;


import MaslyakBank_Token.dao.UserTokenDAO;
import MaslyakBank_Token.entity.TokenTable;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class TokenService {

    private UserTokenDAO userTokenDAO;

    public StringBuilder createToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            token.append(random.nextInt(20));
        }
        return token;
    }

    public TokenTable saveToken(UsersTable user) {
        TokenTable token = new TokenTable();
        token.setUser(user);
        token.setToken(createToken().toString());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setValid(true);
        token.setExpired(false);
        return userTokenDAO.saveToken(token);
    }




}
