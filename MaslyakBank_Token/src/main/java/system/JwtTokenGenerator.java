package system;

import dao.UserTokenDAO;
import entity.TokenTable;
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
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key secret;
    private final UserTokenDAO userTokenDAO;

    @PostConstruct
    public void init() {
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UsersTable user) {
        String token = Jwts.builder()
                .setSubject(user.getLogin())
                .claim("user_id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration( new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 7))//2 h 40 min
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
        saveToken(user, token);
        return token;
    }

    private void saveToken(UsersTable user, String token) {
        TokenTable tokenEntity = new TokenBuilder()
                .withUser(user)
                .token(token)
                .build();
        userTokenDAO.saveToken(tokenEntity);
    }
}
