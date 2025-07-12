package MaslyakBank_Token.system;

import MaslyakBank_Token.dao.UserTokenDAO;
import MaslyakBank_Token.entity.TokenTable;
import dao.UserDAO;
import entity.UsersTable;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserTokenDAO userTokenDAO;


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


    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid (String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
