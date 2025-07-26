package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.VerificationUserStatus;
import util.SecurityUtil;


@Service
@AllArgsConstructor
public class ProfileService {

    private ProfileDAO profileDAO;
    private ProfileMapper profileMapper;
    private VerificationUserStatus verification;

    public ProfileTable createProfile(ProfileRequestDTO dto){
        ProfileTable profile = profileMapper.toEntity(dto);
        UsersTable user = SecurityUtil.getCurrentUser();
        profile.setUser(user);

        verification.checkStatus(user);
        return profileDAO.saveProfile(profile);
    }





}
