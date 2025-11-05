package MaslyakBank_Core.controller.user;


import MaslyakBank_Core.dao.UserFilterRepository;
import MaslyakBank_Core.dao.UserPageDAO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.requests.UserFilterDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationCursorDTO;
import MaslyakBank_Core.dto.response.ResponsePaginationOffsetDTO;
import MaslyakBank_Core.service.user.UserService;
import MaslyakBank_Core.system.UserSpecifications;
import dto.TokenPair;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public ResponsePaginationOffsetDTO getUsersOffset(UserFilterDTO filter,
                                                      @RequestParam(defaultValue = "1")  int page,
                                                      @RequestParam(defaultValue = "createdAt")  String sortBy,
                                                      @RequestParam(defaultValue = "asc")  String sortDir) {
        return userService.getUsersOffset(filter, page, sortBy, sortDir);
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
