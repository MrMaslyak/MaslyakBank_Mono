package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;
import util.SecurityUtil;


@Service
public class ProfileService {

    private ProfileDAO profileDAO;
    private ProfileMapper profileMapper;
    private VerificationUserStatus verification;
    private final RestClient accountRestClient;
    private final UserDAO userDAO;

    public ProfileService(
            @Qualifier("accountRestClient") RestClient accountRestClient,
            ProfileDAO profileDAO,
            ProfileMapper profileMapper,
            VerificationUserStatus verification,
            UserDAO userDAO
    ) {
        this.accountRestClient = accountRestClient;
        this.profileDAO = profileDAO;
        this.profileMapper = profileMapper;
        this.verification = verification;
        this.userDAO = userDAO;
    }

    public ProfileTable createProfile(ProfileRequestDTO dto) {
        UsersTable user = SecurityUtil.getCurrentUser();
        verification.checkStatus(user);

        ProfileTable profile = createAndSaveProfile(dto, user);
        createInitialAccount();

        updateUserStatus(user, UserStatus.COMPLETED);

        return profile;
    }


    private ProfileTable createAndSaveProfile(ProfileRequestDTO dto, UsersTable user) {
        ProfileTable profile = profileMapper.toEntity(dto);
        profile.setUser(user);
        return profileDAO.saveProfile(profile);
    }



    private void updateUserStatus(UsersTable user, UserStatus status) {
        user.setStatus(status);
        userDAO.updateUser(user);
    }


    private void createInitialAccount() {
        try {
            String token = SecurityUtil.getCurrentToken();
            accountRestClient.post()
                    .uri("/create")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new IllegalStateException("Профиль создан, но не удалось создать счёт", e);
        }
    }


}
