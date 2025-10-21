package MaslyakBank_Core.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProfileRequestDTO {

    private String firstName;
    private String lastName;

    private String city;
    private String avatarUrl;
    private String bio;
    private Date birthDay;

}
