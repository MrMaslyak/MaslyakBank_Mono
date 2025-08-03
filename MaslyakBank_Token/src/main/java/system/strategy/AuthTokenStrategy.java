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
public class AuthTokenStrategy implements TokenStrategy{

    private final JwtTokenGenerator generator;
    private final UserTokenDAO userTokenDAO;

    @Override
    public String createToken(Object principal) {
        Authentication auth = (Authentication) principal;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UsersTable user = userDetails.getUser();

        userTokenDAO.deleteToken(user.getLogin());

        return generator.generateToken(user, TokenLifetime.AUTHENTICATION, TokenRole.AUTH);
    }
}
