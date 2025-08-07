package controller;


import dto.CleanerRequestDTO;
import dto.JwtTokenRequestDTO;
import service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;

    @PostMapping("/auth/create")
    public String createAuthToken(@RequestBody JwtTokenRequestDTO dto) {
        return tokenManagmentService.getAuthToken(dto);
    }

    @PostMapping("/registration/create")
    public String createRegistrationToken(@RequestBody String login) {
            return tokenManagmentService.getRegistrationToken(login);
    }






}
