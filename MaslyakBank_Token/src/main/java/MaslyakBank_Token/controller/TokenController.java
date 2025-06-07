package MaslyakBank_Token.controller;


import MaslyakBank_Token.entity.UserTokenTable;
import MaslyakBank_Token.entity.UsersTable;
import MaslyakBank_Token.service.TokenManagmentService;
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
        UserTokenTable token = new UserTokenTable();
        token.setToken(tokenManagmentService.createToken().toString());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setIsValid(true);
        token.setIsExpired(false);
        token.setUser(new UsersTable());

        tokenManagmentService.saveToken(token);
    }



}
