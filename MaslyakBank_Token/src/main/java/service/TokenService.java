package service;



import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.RefreshRequestDTO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UsersTable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import dao.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import system.JwtTokenGenerator;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TokenService {



    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final UserTokenDAO tokenDAO;
    private final JwtTokenGenerator tokenGenerator;


    public TokenPair getToken(String login) {
        UsersTable user = userDAO.findByLogin(login);
        return tokenGenerator.generateTokenPair(user);
    }

    public TokenPair getAuthToken(JwtTokenRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            if (tokenDAO.findTokenByUser(userDAO.findByLogin(dto.getLogin()))){
                tokenDAO.deleteToken(dto.getLogin());
            }
            return getToken(dto.getLogin());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

    public TokenPair getTokenPair(RefreshRequestDTO dto){
        var refresh = tokenDAO.findRefreshToken(dto.getRefresh())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (refresh.getExpiredAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        UsersTable user = refresh.getUser();
        return tokenGenerator.generateTokenPair(user);

    }

}
