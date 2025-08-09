package MaslyakBank_Core.controller.user;

import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import MaslyakBank_Core.service.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/profilemanagment/profile")
public class ProfileController {

    private final ProfileService profileService;

        @PostMapping("/create")
        public ProfileTable createProfile(@RequestBody ProfileRequestDTO dto) {
           return profileService.createProfile(dto);
        }
}
