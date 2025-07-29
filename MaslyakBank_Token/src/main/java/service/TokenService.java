package service;


import dto.JwtTokenRequestDTO;
import enums.TokenLifetime;
import mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import system.JwtTokenGenerator;
import dao.UserDAO;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {


    private final JwtTokenGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;

    public String getAuthToken(JwtTokenRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return jwtGenerator.generateToken(auth);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

    public String getRegistrationToken(String login) {
        UsersTable user = userDAO.findByLogin(login);
        return jwtGenerator.generateToken(user, TokenLifetime.REGISTRATION);
    }






}
