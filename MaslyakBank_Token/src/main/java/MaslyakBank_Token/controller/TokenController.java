package MaslyakBank_Token.controller;


import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.service.TokenManagmentService;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment")
public class TokenController {

    private final TokenManagmentService tokenManagmentService;


    @PostMapping("/create")
    public StringBuilder createToken() {
        return tokenManagmentService.createToken();
    }

    @PostMapping("/save")
    public void saveToken() {
        TokenTable token = new TokenTable();
        token.setToken(tokenManagmentService.createToken().toString());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setValid(true);
        token.setExpired(false);
        token.setUser(new UsersTable());

        tokenManagmentService.saveToken(token);
    }



}
