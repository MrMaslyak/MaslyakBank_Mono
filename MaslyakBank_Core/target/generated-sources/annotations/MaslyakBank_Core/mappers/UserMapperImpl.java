package MaslyakBank_Core.mappers;

import MaslyakBank_Core.dto.requests.RegistrationRequestDTO;
import entity.UsersTable;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-01T18:17:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Homebrew)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UsersTable toEntity(RegistrationRequestDTO userDataDTO) {
        if ( userDataDTO == null ) {
            return null;
        }

        UsersTable usersTable = new UsersTable();

        usersTable.setLogin( userDataDTO.getLogin() );
        usersTable.setPassword( userDataDTO.getPassword() );
        usersTable.setEmail( userDataDTO.getEmail() );
        usersTable.setPhoneNumber( userDataDTO.getPhoneNumber() );

        usersTable.setCreatedAt( new Date() );
        usersTable.setUpdatedAt( new Date() );

        return usersTable;
    }
}
