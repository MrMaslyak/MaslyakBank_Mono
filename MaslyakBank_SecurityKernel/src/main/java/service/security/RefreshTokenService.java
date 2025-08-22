package service.security;


import dao.RefreshTokenDAO;
import entity.RefreshTokenTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {



    private final RefreshTokenDAO refreshTokenDAO;

    public boolean validate(String refreshToken) {
        System.out.println("Validating refresh token: " + refreshToken);

        if (refreshToken == null || refreshToken.isEmpty()) {
            return false;
        }

        Optional<RefreshTokenTable> tokenOpt = refreshTokenDAO.findByToken(refreshToken);

        System.out.println("Found token in DB: " + tokenOpt.isPresent());

        if (tokenOpt.isEmpty()) {
            return false;
        }

        RefreshTokenTable token = tokenOpt.get();

        if (token.getExpiredAt() == null || token.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (token.isRevoked()) {
            return false;
        }

        return true;
    }

}
