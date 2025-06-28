package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserDAO;
import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.LoginRequestDTO;
import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import MaslyakBank_Core.dto.response.ResponseDTO;
import MaslyakBank_Core.mappers.UserMapper;
import entity.UsersTable;
import enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public ResponseDTO registration(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        user.setStatus(UserStatus.REGISTERED);
        UsersTable savedUser = userDAO.registrationUser(user);
        return new ResponseDTO("Registration", true, savedUser);
    }

    public DeleteUsersDTO deleteUser(DeleteUsersDTO login) {
        return userDAO.deleteUser(login);
    }

    public ResponseDTO login(LoginRequestDTO dto) {
        UsersTable user = userDAO.login(dto);

        if (user == null) {
            return new ResponseDTO("User not found or credentials incorrect", false, null);
        }

        if (user.getStatus() != UserStatus.COMPLETED) {
            return new ResponseDTO("Registration is not completed", false, user);
        }

        return new ResponseDTO("Login", true, user);
    }

}
