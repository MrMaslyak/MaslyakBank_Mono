package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProfileService {

    private ProfileDAO profileDAO;
    private UserDAO userDAO;
    private ProfileMapper profileMapper;

    public ProfileTable createProfile(ProfileRequestDTO dto) {
        UsersTable user = userDAO.findById(dto.getUserId());
        ProfileTable profile = profileMapper.toEntity(dto);
        profile.setUser(user);
        if (user.getStatus() == UserStatus.PARTIALLY_COMPLETED){
            user.setStatus(UserStatus.COMPLETED);
        }else {
            user.setStatus(UserStatus.PARTIALLY_COMPLETED);
        }
        userDAO.updateUser(user);
        return profileDAO.saveProfile(profile);
    }


}
