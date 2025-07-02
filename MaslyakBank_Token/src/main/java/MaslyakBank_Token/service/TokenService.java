package MaslyakBank_Token.service;


import MaslyakBank_Token.dao.UserTokenDAO;
import MaslyakBank_Token.dto.TokenRequestDTO;
import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.system.TokenBuilder;
import dao.UserDAO;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class TokenService {

    private UserTokenDAO userTokenDAO;
    private UserDAO userDAO;
    private TokenBuilder tokenBuilder;


    public TokenTable saveToken(TokenRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        TokenTable token = tokenBuilder
                .withUser(user)
                .authToken()
                .build();
        return userTokenDAO.saveToken(token);
    }




}
