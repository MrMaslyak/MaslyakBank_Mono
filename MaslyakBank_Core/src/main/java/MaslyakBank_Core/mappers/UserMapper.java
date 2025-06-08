package MaslyakBank_Core.mappers;


import MaslyakBank_Core.dto.RegistrationRequestDTO;
import MaslyakBank_Core.entity.UsersTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Mapper(componentModel = "spring", imports = Date.class)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new Date())")
    @Mapping(target = "updatedAt", expression = "java(new Date())")
    UsersTable toEntity(RegistrationRequestDTO userDataDTO);


}
