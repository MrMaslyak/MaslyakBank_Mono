package MaslyakBank_Token.controller;


import MaslyakBank_Token.dto.TokenRequestDTO;
import MaslyakBank_Token.entity.TokenTable;

import MaslyakBank_Token.service.TokenService;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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



}
