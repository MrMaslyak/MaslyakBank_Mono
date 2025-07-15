package MaslyakBank_Core.controller;


import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseDTO registration(@RequestBody RegistrationRequestDTO dto) {
        String token = userService.requestRegistrationToken(dto);
        return new ResponseDTO("Registration successful", true, token);
    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody JwtTokenRequestDTO dto) {
        String token = userService.requestAuthToken(dto);
        return new ResponseDTO("Login successful", true, token);
    }

    @DeleteMapping("/delete")
    public DeleteUsersDTO deleteUser(@RequestBody DeleteUsersDTO login) {
        return userService.deleteUser(login);
    }



}
