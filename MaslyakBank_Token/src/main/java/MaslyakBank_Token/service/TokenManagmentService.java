package MaslyakBank_Token.service;


import MaslyakBank_Token.dao.UserTokenDAO;
import MaslyakBank_Token.entity.UserTokenTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class TokenManagmentService {

    private UserTokenDAO userTokenDAO;

    public StringBuilder createToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            token.append(random.nextInt(20));
        }
        return token;
    }

    public UserTokenTable saveToken(UserTokenTable userToken) {
        return userTokenDAO.saveToken(userToken);
    }




}
