package service;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UsersTable;
import errors.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import dao.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import system.JwtTokenGenerator;
import system.validators.RefreshTokenValidator;
import util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

    private final RefreshTokenValidator refreshTokenValidator;
    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final UserTokenDAO tokenDAO;
    private final RefreshTokenDAO refreshTokenDAO;
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

    public TokenPair getTokenPair(String refreshToken) {
        RefreshTokenTable token = refreshTokenValidator.validate(refreshToken);

        UsersTable user = token.getUserTokenTable().getUser();

        refreshTokenDAO.deleteByUserId(user.getId());
        tokenDAO.deleteByUserId(user.getId());

        return tokenGenerator.generateTokenPair(user);
    }

    public void logout() {
        UsersTable user = SecurityUtil.getCurrentUser();
        tokenDAO.deleteByUserId(user.getId());
        refreshTokenDAO.deleteByUserId(user.getId());
    }


    public void adminLogout(String login) {
        UsersTable user = userDAO.findByLogin(login);
        tokenDAO.deleteByUserId(user.getId());
        refreshTokenDAO.deleteByUserId(user.getId());
    }
}
