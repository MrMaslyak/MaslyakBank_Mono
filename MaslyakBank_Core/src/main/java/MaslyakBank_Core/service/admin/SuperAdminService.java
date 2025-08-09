package MaslyakBank_Core.service.admin;

import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.requests.admin.SAdminListDTO;
import enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final UserSecurityDAO userDAO;

    public void grantAdminRole(SAdminListDTO sAdminListDTO) {
        userDAO.updateUserRole(sAdminListDTO, UserRole.ADMIN);
    }

    public void revokeAdminRole(SAdminListDTO sAdminListDTO) {
        userDAO.updateUserRole(sAdminListDTO, UserRole.USER);
    }

}
