package MaslyakBank_Core.controller.admin;

import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.admin.LogoutUserDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/admin")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/delete")
    public DeleteUsersDTO deleteUser(@RequestBody DeleteUsersDTO login) {
        return adminService.deleteUser(login);
    }

    @PostMapping("/logout")
    public ResponseDTO logoutUser(@RequestBody LogoutUserDTO dto) {
        adminService.logoutUser(dto);
        return new ResponseDTO("Logout "  + dto + " successful", true, null);
    }
}
