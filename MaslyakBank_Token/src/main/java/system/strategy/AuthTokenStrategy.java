package system.strategy;

import dao.UserTokenDAO;
import details.CustomUserDetails;
import entity.UsersTable;
import enums.TokenLifetime;
import enums.TokenRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import system.JwtTokenGenerator;

@Component
@RequiredArgsConstructor
public class AuthTokenStrategy implements TokenStrategy<Authentication>{

    private final JwtTokenGenerator generator;
    private final UserTokenDAO userTokenDAO;

    @Override
    public String createToken(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UsersTable user = userDetails.getUser();

       if (userTokenDAO.findTokenByUser(user)){
           userTokenDAO.deleteToken(user.getLogin());
       }
        return generator.generateToken(user, TokenLifetime.AUTHENTICATION, TokenRole.AUTH);
    }
}
