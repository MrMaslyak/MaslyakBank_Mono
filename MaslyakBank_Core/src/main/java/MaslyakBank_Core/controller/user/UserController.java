package MaslyakBank_Core.controller.user;


import MaslyakBank_Core.dao.UserPageDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationDTO;
import MaslyakBank_Core.service.user.UserService;
import dto.TokenPair;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/user")
public class UserController {

    private final UserService userService;
    private final UserPageDAO userPageDAO;

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

    @GetMapping("/get")
    public ResponsePaginationDTO getUsers(@RequestParam(defaultValue = "1")  int page) {
        int size = 10;
        int offset = (page - 1) * size;

        List<UsersTable> users = userPageDAO.findUsersPage(size, offset);

        int totalElements = userPageDAO.countUsers();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new ResponsePaginationDTO(page, size, totalPages, totalElements, users);
    }





}
