package MaslyakBank_Token.controller;


import MaslyakBank_Token.dto.TokenRequestDTO;
import MaslyakBank_Token.dto.TokenValidationResponseDTO;
import MaslyakBank_Token.entity.TokenTable;

import MaslyakBank_Token.service.TokenService;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/validation")
    public ResponseEntity<TokenValidationResponseDTO> validateToken(@RequestHeader("Maslyak-Token") String token) {
        try {
            tokenManagmentService.validateToken(token);
            return ResponseEntity.ok(new TokenValidationResponseDTO(true, "Token is valid"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new TokenValidationResponseDTO(false, ex.getMessage()));
        }
    }



}
