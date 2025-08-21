package controller;


import dto.JwtTokenRequestDTO;
import dto.RefreshRequestDTO;
import dto.TokenPair;
import service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;

    @PostMapping("/create")
    public TokenPair createToken(@RequestBody String login) {
            return tokenManagmentService.getToken(login);
    }

    @PostMapping("auth/create")
    public TokenPair createAuthToken(@RequestBody JwtTokenRequestDTO dto) {
        return tokenManagmentService.getAuthToken(dto);
    }

    @PostMapping("/refresh")
    public TokenPair getTokenPair(RefreshRequestDTO dtp) {
        return tokenManagmentService.getTokenPair(dto);
    }








}
