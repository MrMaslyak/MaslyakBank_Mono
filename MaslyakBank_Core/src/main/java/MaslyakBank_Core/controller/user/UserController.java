package MaslyakBank_Core.controller.user;


import MaslyakBank_Core.dao.UserPageDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationCursorDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationOffsetDTO;
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

    @GetMapping("pagination/offset/get")
    public ResponsePaginationOffsetDTO getUsersOffset(@RequestParam(defaultValue = "1")  int page) {
        int size = 10;
        int offset = (page - 1) * size;

        List<UsersTable> users = userPageDAO.findUsersPage(size, offset);

        int totalElements = userPageDAO.countUsers();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new ResponsePaginationOffsetDTO(page, size, totalPages, totalElements, users);
    }

    @GetMapping("pagination/cursor/get")
    public ResponsePaginationCursorDTO getUsersCursor(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String cursor) {

        int totalElements = userPageDAO.countUsers();

        List<UsersTable> users;
        String nextCursor;

        if (cursor == null) {
            users = userService.getFirstPage(limit);
        } else {
            users = userService.getNextPage(limit, cursor);
        }

        nextCursor = users.isEmpty() ? null : users.getLast().getLogin();

        return new ResponsePaginationCursorDTO(limit, totalElements, users, nextCursor);
    }






}
