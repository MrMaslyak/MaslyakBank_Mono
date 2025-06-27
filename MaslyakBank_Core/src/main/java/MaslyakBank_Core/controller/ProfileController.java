package MaslyakBank_Core.controller;

import MaslyakBank_Core.dto.ProfileRequestDTO;
import MaslyakBank_Core.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/profilemanagment/profile")
public class ProfileController {

    private final ProfileService profileService;

        @PostMapping("/create")
        public void createProfile(@RequestBody ProfileRequestDTO dto) {
            profileService.createProfile(dto);
        }
}
