package MaslyakBank_Core.service;


import MaslyakBank_Core.dao.UserDAO;
import MaslyakBank_Core.dto.RegistrationRequestDTO;
import MaslyakBank_Core.entity.UsersTable;
import MaslyakBank_Core.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UsersTable registration(RegistrationRequestDTO dto) {
        UsersTable user = userMapper.toEntity(dto);
        return userDAO.registrationUser(user);
    }
}
