package MaslyakBank_Token.service;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TokenManagmentService {



    public StringBuilder createToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            token.append(random.nextInt(20));
        }
        return token;
    }


}
