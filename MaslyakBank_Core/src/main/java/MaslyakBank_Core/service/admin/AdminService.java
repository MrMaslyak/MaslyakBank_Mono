package MaslyakBank_Core.service.admin;

import MaslyakBank_Core.dao.UserSecurityDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.admin.LogoutUserDTO;
import MaslyakBank_Core.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import util.SecurityUtil;

@Service
public class AdminService {

    private final UserSecurityDAO userDAO;
    private final RestClient tokenRestClient;

    public AdminService(@Qualifier("tokenRestClient") RestClient tokenRestClient, UserSecurityDAO userDAO) {
        this.tokenRestClient = tokenRestClient;
        this.userDAO = userDAO;
    }


    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUsers(login);
    }






}
