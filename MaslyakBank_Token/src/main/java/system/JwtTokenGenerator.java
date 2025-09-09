package system;

import dao.RefreshTokenDAO;
import dao.UserTokenDAO;
import dto.TokenPair;
import entity.RefreshTokenTable;
import entity.UserTokenTable;
import entity.UsersTable;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key secret;
    private final UserTokenDAO userTokenDAO;
    private final RefreshTokenDAO refreshTokenDAO;

    @PostConstruct
    public void init() {
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenPair generateTokenPair(UsersTable user) {
        String access = generateAccessToken(user);
        String refresh = generateRefreshToken();

       UserTokenTable tokenTable = saveAccessToken(user, access);
       saveRefreshToken(tokenTable, refresh);

        return new TokenPair(access, refresh);
    }



    private String generateAccessToken(UsersTable user) {
        return  Jwts.builder()
                .setSubject(user.getLogin())
                .claim("user_id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration( new Date(System.currentTimeMillis() + 20 * 60 * 1000))//5 min
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(){//256-бит случайных байтов
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private UserTokenTable saveAccessToken(UsersTable user, String token) {
        UserTokenTable tokenEntity = new UserTokenBuilder()
                .withUser(user)
                .token(token)
                .build();
        return userTokenDAO.saveToken(tokenEntity);
    }

    private void saveRefreshToken(UserTokenTable userTokenTable, String refreshToken) {
        RefreshTokenTable refreshTokenEntity = new RefreshTokenBuilder()
                .withUserToken(userTokenTable)
                .token(refreshToken)
                .build();
        refreshTokenDAO.saveToken(refreshTokenEntity);
    }
}
