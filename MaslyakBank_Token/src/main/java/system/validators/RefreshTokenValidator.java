package system.validators;


import dao.RefreshTokenDAO;
import entity.RefreshTokenTable;
import errors.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final RefreshTokenDAO refreshTokenDAO;

    public RefreshTokenTable validate(String refreshToken) {
        RefreshTokenTable token = getToken(refreshToken);
        checkRevoked(token);
        checkExpired(token);
        return token;
    }

    private RefreshTokenTable getToken(String refreshToken) {
        return refreshTokenDAO.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found or invalid"));
    }

    private void checkRevoked(RefreshTokenTable token) {
        if (token.isRevoked()) {
            throw new RefreshTokenException("Refresh token revoked");
        }
    }

    private void checkExpired(RefreshTokenTable token) {
        if (token.getExpiredAt() == null || token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token expired");
        }
    }
}
