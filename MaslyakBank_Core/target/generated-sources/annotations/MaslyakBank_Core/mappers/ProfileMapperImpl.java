package MaslyakBank_Core.mappers;

import MaslyakBank_Core.dto.requests.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-01T18:17:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Homebrew)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public ProfileTable toEntity(ProfileRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProfileTable profileTable = new ProfileTable();

        profileTable.setFirstName( dto.getFirstName() );
        profileTable.setLastName( dto.getLastName() );
        profileTable.setCity( dto.getCity() );
        profileTable.setAvatarUrl( dto.getAvatarUrl() );
        profileTable.setBio( dto.getBio() );
        profileTable.setBirthDay( dto.getBirthDay() );

        profileTable.setCreatedAt( new Date() );
        profileTable.setUpdatedAt( new Date() );
        profileTable.setCompleted( true );

        return profileTable;
    }
}
