package MaslyakBank_Token.controller;


import MaslyakBank_Token.dto.JwtTokenRequestDTO;
import MaslyakBank_Token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;

    @PostMapping("/create")
    public String createToken(JwtTokenRequestDTO dto) {
        return tokenManagmentService.getToken(dto);
    }




}
