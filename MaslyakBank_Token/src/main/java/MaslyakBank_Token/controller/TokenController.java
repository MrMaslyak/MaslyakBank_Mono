package MaslyakBank_Token.controller;


import MaslyakBank_Token.dto.TokenRequestDTO;

import MaslyakBank_Token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;


    @PostMapping("/auth/save")
    public void saveAuthToken(@RequestBody TokenRequestDTO dto) {
        tokenManagmentService.saveAuthToken(dto);
    }

    @PostMapping("/registration/save")
    public void saveRegistrationToken(@RequestBody TokenRequestDTO dto) {
        tokenManagmentService.saveRegistrationToken(dto);
    }

    @PostMapping("/validation")
    public TokenRequestDTO validateToken(@RequestHeader("Maslyak-Token") String token) {
        return tokenManagmentService.validateToken(token);
    }


}
