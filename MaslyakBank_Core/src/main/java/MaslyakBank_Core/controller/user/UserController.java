package MaslyakBank_Core.controller.user;


import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.service.user.UserService;
import dto.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseDTO registration(@RequestBody RegistrationRequestDTO dto) {
        TokenPair token = userService.requestRegistrationToken(dto);
        return new ResponseDTO("Registration successful", true, token);
    }



    @PostMapping("/login")
    public ResponseDTO login(@RequestBody JwtTokenRequestDTO dto) {
        TokenPair token = userService.sendAuthRequest(dto);
        return new ResponseDTO("Login successful", true, token);
    }

    @PostMapping("/logout")
    public ResponseDTO logout() {
        userService.sendLogoutRequest();
        return new ResponseDTO("Logout successful", true, null);
    }





}
