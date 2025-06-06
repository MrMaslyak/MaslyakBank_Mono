package MaslyakBank_Token.controller;


import MaslyakBank_Token.service.TokenManagmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment")
public class TokenController {

    private final TokenManagmentService tokenManagmentService;


    @GetMapping("/create")
    public StringBuilder createToken() {
        return tokenManagmentService.createToken();
    }



}
