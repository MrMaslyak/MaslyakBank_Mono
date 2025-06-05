package MaslyakBank_Core.controller;


import MaslyakBank_Core.dto.RegistrationRequestDTO;
import MaslyakBank_Core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public RegistrationRequestDTO registration(@RequestHeader("login") String login,
                                               @RequestHeader("email") String email,
                                               @RequestHeader("password") String password,
                                               @RequestHeader("phoneNumber") String phoneNumber) {
        return userService.registration(login, email, password, phoneNumber);
    }
}
