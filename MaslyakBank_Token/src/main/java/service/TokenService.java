package service;



import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import entity.UsersTable;
import enums.TokenLifetime;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import dao.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import system.JwtTokenGenerator;

@Service
@AllArgsConstructor
public class TokenService {



    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final UserTokenDAO tokenDAO;
    private final JwtTokenGenerator tokenGenerator;


    public String getToken(String login, TokenLifetime lifetime) {
        UsersTable user = userDAO.findByLogin(login);
        return tokenGenerator.generateToken(user, lifetime);
    }

    public String getAuthToken(JwtTokenRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            if (tokenDAO.findTokenByUser(userDAO.findByLogin(dto.getLogin()))){
                tokenDAO.deleteToken(dto.getLogin());
            }
            return getToken(dto.getLogin(), TokenLifetime.AUTHENTICATION);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

}
