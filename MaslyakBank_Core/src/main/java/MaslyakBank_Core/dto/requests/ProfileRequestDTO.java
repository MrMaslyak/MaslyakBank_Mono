package MaslyakBank_Core.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProfileRequestDTO {

    private UUID userId;

    private String firstName;
    private String lastName;

    private String city;
    private String avatarUrl;
    private String bio;
    private Date birthDay;

}
