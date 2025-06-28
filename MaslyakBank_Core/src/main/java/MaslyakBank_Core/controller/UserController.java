package MaslyakBank_Core.controller;


import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.LoginRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.service.UserService;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registration(@RequestBody RegistrationRequestDTO dto) {
        ResponseDTO response = userService.registration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        ResponseDTO response = userService.login(dto);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public DeleteUsersDTO deleteUser(@RequestBody DeleteUsersDTO login) {
        return userService.deleteUser(login);
    }



}
