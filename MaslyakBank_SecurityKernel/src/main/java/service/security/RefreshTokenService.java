package service.security;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenDAO refreshTokenRepository;

    public boolean validate(String refresh) {
        return refreshTokenRepository.findByToken(refresh)
                .filter(rt -> rt.getExpiredAt().isAfter(Instant.now()));
    }
}
