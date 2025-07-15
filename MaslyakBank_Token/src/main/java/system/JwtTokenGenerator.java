package system;

import dao.UserTokenDAO;
import entity.TokenTable;
import entity.UsersTable;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager authenticationManager;


    @PostConstruct
    public void init() {
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UsersTable user){
        String token = Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();

        TokenTable tokenTable = new TokenBuilder()
                .withUser(user)
                .token(token)
                .build();

        userTokenDAO.saveToken(tokenTable);

        return token;
    }


}
