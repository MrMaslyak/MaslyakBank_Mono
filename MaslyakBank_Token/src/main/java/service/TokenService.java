package service;


import mappers.UserMapper;
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
    private UserMapper  userMapper;

//    public String getAuthToken(JwtTokenRequestDTO dto) {
//        try {
//            Authentication auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
//            );
//            return jwtProvider.generateToken(auth);
//        } catch (BadCredentialsException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
//        }
//    }

    public String getRegistrationToken(String login) {
        UsersTable user = userDAO.findByLogin(login);
        return jwtGenerator.generateToken(user);
    }






}
