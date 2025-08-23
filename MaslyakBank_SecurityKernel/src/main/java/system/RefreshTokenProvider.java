package system;


import dao.RefreshTokenDAO;
import entity.RefreshTokenTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenProvider {

    private final RefreshTokenDAO refreshTokenDAO;

    public boolean isValid(String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) return false;

        Optional<RefreshTokenTable> tokenOpt = refreshTokenDAO.findByToken(refreshToken);

        System.out.println("Found token in DB: " + tokenOpt.isPresent());

        if (tokenOpt.isEmpty()) return false;

        RefreshTokenTable token = tokenOpt.get();

        boolean expired = token.getExpiredAt() == null || token.getExpiredAt().isBefore(LocalDateTime.now());
        boolean revoked = token.isRevoked();

        return !expired && !revoked;
    }

}
