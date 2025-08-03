package system.strategy;

import entity.UsersTable;
import enums.TokenLifetime;
import enums.TokenRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.JwtTokenGenerator;

@Component
@RequiredArgsConstructor
public class RegistrationTokenStrategy implements TokenStrategy{

    private final JwtTokenGenerator generator;

    @Override
    public String createToken(Object principal) {
        UsersTable user = (UsersTable) principal;
        return generator.generateToken(user, TokenLifetime.REGISTRATION, TokenRole.REGISTRATION);
    }
}
