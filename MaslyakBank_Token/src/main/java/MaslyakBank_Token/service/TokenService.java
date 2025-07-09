package MaslyakBank_Token.service;


import MaslyakBank_Token.dto.JwtTokenRequestDTO;
import MaslyakBank_Token.system.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {


    private final JwtTokenProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public String getToken(JwtTokenRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
        );
        return jwtProvider.generateToken(auth);
    }




}
