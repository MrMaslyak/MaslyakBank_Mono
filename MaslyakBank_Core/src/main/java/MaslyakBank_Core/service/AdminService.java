package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserSecurityDAO userDAO;

    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUser(login);
    }



}
