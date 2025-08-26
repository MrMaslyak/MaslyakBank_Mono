package service;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.JwtTokenRequestDTO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UserTokenTable;
import entity.UsersTable;
import enums.TokenStatus;
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

import java.util.List;

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
            UsersTable user = userDAO.findByLogin(dto.getLogin());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            List<UserTokenTable> tokens = tokenDAO.findTokensByUser(user);
            if (!tokens.isEmpty()) {
                invalidateTokens(user);
            }
            return getToken(dto.getLogin());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }
    public TokenPair refreshOrLogout(String refreshToken) {
        UsersTable user;

        if (refreshToken == null || refreshToken.isBlank()) {
            user = SecurityUtil.getCurrentUser();
        } else {
            RefreshTokenTable token = refreshTokenValidator.validate(refreshToken);
            user = token.getUserTokenTable().getUser();
        }

        invalidateTokens(user);

        if (refreshToken == null || refreshToken.isBlank()) {
            return null; // чистый logout
        }


        return tokenGenerator.generateTokenPair(user);
    }



    private void invalidateTokens(UsersTable user) {
        // access
        tokenDAO.findAllByUserId(user.getId()).forEach(token -> {
            token.setExpired(true);
            token.setValid(false);
            token.setStatus(TokenStatus.EXPIRED);
            tokenDAO.saveToken(token);
        });

        // refresh
        refreshTokenDAO.findAllByUserId(user.getId()).forEach(refresh -> {
            refresh.setRevoked(true);
            refreshTokenDAO.saveToken(refresh);
        });
    }



}
