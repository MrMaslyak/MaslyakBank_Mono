package MaslyakBank_Core.controller;

import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/delete")
    public DeleteUsersDTO deleteUser(@RequestBody DeleteUsersDTO login) {
        return adminService.deleteUser(login);
    }
}
