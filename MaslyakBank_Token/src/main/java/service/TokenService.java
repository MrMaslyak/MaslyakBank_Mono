package service;



import dao.RefreshTokenDAO;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {



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
        Optional<RefreshTokenTable> oldRefreshOpt = refreshTokenDAO.findByToken(refreshToken);

        System.out.println("Refresh token found: " + oldRefreshOpt.isPresent());

        if (oldRefreshOpt.isEmpty()) {
            System.out.println("Refresh token not found: " + refreshToken);
            throw new RuntimeException("Refresh token not found or invalid");
        }

        RefreshTokenTable token = oldRefreshOpt.get();
        System.out.println("Refresh token found: id=" + token.getId() + ", revoked=" + token.isRevoked() + ", expiresAt=" + token.getExpiredAt());

        if (token.isRevoked()) {
            System.out.println("Refresh token revoked");
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiredAt() == null || token.getExpiredAt().isBefore(LocalDateTime.now())) {
            System.out.println("Refresh token expired");
            throw new RuntimeException("Refresh token expired");
        }

        UsersTable user = token.getUserTokenTable().getUser();

        // Удаляем или деактивируем старые токены
        refreshTokenDAO.deleteByUserId(user.getId());
        tokenDAO.deleteByUserId(user.getId());

        // Генерируем и сохраняем новую пару
        return tokenGenerator.generateTokenPair(user);
    }


}
