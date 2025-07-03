package MaslyakBank_Token.service;


import MaslyakBank_Token.dao.UserTokenDAO;
import MaslyakBank_Token.dto.TokenRequestDTO;
import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.enums.TokenRole;
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


    public TokenTable saveAuthToken(TokenRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        TokenTable authToken = tokenBuilder
                .withUser(user)
                .withRole(TokenRole.AUTH)
                .token(tokenBuilder::createAuthToken)
                .build();
        return userTokenDAO.saveToken(authToken);
    }

    public TokenTable saveRegistrationToken(TokenRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        TokenTable registToken = tokenBuilder
                .withUser(user)
                .withRole(TokenRole.REGISTRATION)
                .token(tokenBuilder::createRegistToken)
                .build();
        return userTokenDAO.saveToken(registToken);
    }




}
