package service;



import dto.JwtTokenRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import dao.UserDAO;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import system.strategy.AuthTokenStrategy;
import system.strategy.RegistrationTokenStrategy;

@Service
@AllArgsConstructor
public class TokenService {



    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final AuthTokenStrategy authStrategy;
    private final RegistrationTokenStrategy registrationStrategy;

    public String getAuthToken(JwtTokenRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            return authStrategy.createToken(auth);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

    public String getRegistrationToken(String login) {
        UsersTable user = userDAO.findByLogin(login);
        return registrationStrategy.createToken(user);
    }

}
