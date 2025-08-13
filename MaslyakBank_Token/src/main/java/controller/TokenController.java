package controller;


import dto.JwtTokenRequestDTO;
import enums.TokenLifetime;
import service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;

    @PostMapping("/create")
    public String createToken(@RequestBody String login) {
            return tokenManagmentService.getToken(login, TokenLifetime.REGISTRATION);
    }

    @PostMapping("auth/create")
    public String createToken(@RequestBody JwtTokenRequestDTO dto) {
        return tokenManagmentService.getAuthToken(dto);
    }








}
