package MaslyakBank_Core.mappers;

import MaslyakBank_Core.dto.ProfileRequestDTO;
import MaslyakBank_Core.entity.ProfileTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper(componentModel = "spring", imports = Date.class)
public interface ProfileMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new Date())")
    @Mapping(target = "updatedAt", expression = "java(new Date())")
    @Mapping(target = "completed", constant = "true")
    ProfileTable toProfileTable(ProfileRequestDTO dto);
}
