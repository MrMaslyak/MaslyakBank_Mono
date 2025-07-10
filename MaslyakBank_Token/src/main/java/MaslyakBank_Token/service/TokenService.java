package MaslyakBank_Token.service;


import MaslyakBank_Token.details.CustomUserDetails;
import MaslyakBank_Token.dto.JwtTokenRequestDTO;
import MaslyakBank_Token.dto.RegistrationRequestDTO;
import MaslyakBank_Token.mappers.UserMapper;
import MaslyakBank_Token.system.JwtTokenProvider;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class TokenService {


    private final JwtTokenProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private UserMapper  userMapper;

    public String getAuthToken(JwtTokenRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            return jwtProvider.generateToken(auth);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

    public String getRegistrationToken(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        return jwtProvider.generateToken(auth);
    }






}
