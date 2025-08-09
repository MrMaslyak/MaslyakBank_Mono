package MaslyakBank_Core.controller.admin;

import MaslyakBank_Core.dto.requests.admin.SAdminListDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.service.admin.SuperAdminService;
import dao.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
    @RequestMapping("/maslyakbank/super-admin")
public class SuperAdminController {

    private final SuperAdminService service;

    @PostMapping("/grant-admin")
    public ResponseDTO makeAdmin(@RequestBody SAdminListDTO sAdminListDTO) {
        service.grantAdminRole(sAdminListDTO);
        return new ResponseDTO("User promoted to admin", true, null);
    }

    @PostMapping("/revoke-admin")
    public ResponseDTO revokeAdmin(@RequestBody SAdminListDTO sAdminListDTO) {
        service.revokeAdminRole(sAdminListDTO);
        return new ResponseDTO("Admin rights revoked", true, null);
    }


}
