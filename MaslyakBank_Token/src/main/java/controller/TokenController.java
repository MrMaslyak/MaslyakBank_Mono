package controller;


import dto.CleanerRequestDTO;
import dto.JwtTokenRequestDTO;
import service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import system.jobs.CleanerJob;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/tokenmanagment/token")
public class TokenController {

    private final TokenService tokenManagmentService;
    private final CleanerJob cleanerJob;

    @PostMapping("/auth/create")
    public String createAuthToken(@RequestBody JwtTokenRequestDTO dto) {
        return tokenManagmentService.getAuthToken(dto);
    }

    @PostMapping("/registration/create")
    public String createRegistrationToken(@RequestBody String login) {
            return tokenManagmentService.getRegistrationToken(login);
    }

    @PostMapping("/cleaner/toggle")
    public String cleanToken(@RequestBody CleanerRequestDTO dto) {
        cleanerJob.setEnabled(dto.isEnable());
        return dto.isEnable() ? "Cleaner enabled" : "Cleaner disabled";
    }





}
