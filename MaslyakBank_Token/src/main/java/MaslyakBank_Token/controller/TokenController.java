package MaslyakBank_Token.controller;


import MaslyakBank_Token.dto.JwtTokenRequestDTO;
import MaslyakBank_Token.dto.RegistrationRequestDTO;
import MaslyakBank_Token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;

    @PostMapping("/auth/create")
    public String createAuthToken(JwtTokenRequestDTO dto) {
        return tokenManagmentService.getAuthToken(dto);
    }

    @PostMapping("/registration/create")
    public String createRegistrationToken(RegistrationRequestDTO dto) {
        return tokenManagmentService.getRegistrationToken(dto);
    }





}
