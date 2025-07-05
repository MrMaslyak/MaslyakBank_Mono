package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import dao.UserDAO;
import dto.TokenRequestDTO;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;


@Service
@AllArgsConstructor
public class ProfileService {

    private ProfileDAO profileDAO;
    private UserDAO userDAO;
    private ProfileMapper profileMapper;
    private VerificationUserStatus verification;
    private RestClient tokenRestClient;

    public ProfileTable createProfile(ProfileRequestDTO dto, String token) {
        UsersTable user = validationToken(token);
        ProfileTable profile = profileMapper.toEntity(dto);
        profile.setUser(user);

        verification.checkStatus(user);

        userDAO.updateUser(user);
        return profileDAO.saveProfile(profile);
    }

    private UsersTable validationToken(String token) {
        try {
            TokenRequestDTO dto = tokenRestClient.post()
                    .uri("/validation")
                    .header("Maslyak-Token", token)
                    .retrieve()
                    .body(TokenRequestDTO.class);

            return userDAO.findById(dto.getUserId());

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Token validation failed: " + ex.getResponseBodyAsString());
        }
    }




}
