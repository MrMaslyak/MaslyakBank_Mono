package MaslyakBank_Core.service;

import MaslyakBank_Core.dao.ProfileDAO;
import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.dto.response.TokenValidationResponseDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.mappers.ProfileMapper;
import dao.UserDAO;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
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
        UsersTable user = userDAO.findById(dto.getUserId());
        ProfileTable profile = profileMapper.toEntity(dto);
        profile.setUser(user);

        validationToken(token);
        verification.checkStatus(user);

        userDAO.updateUser(user);
        return profileDAO.saveProfile(profile);
    }

    private void validationToken(String token) {
        TokenValidationResponseDTO response = tokenRestClient.post()
                .uri("/validation")
                .header("Maslyak-Token", token)
                .retrieve()
                .body(TokenValidationResponseDTO.class);

        if (response == null || !response.isValid()) {
            throw new RuntimeException("Token is not valid: " +
                    (response != null ? response.getMessage() : "no response body"));
        }
    }



}
