package MaslyakBank_Account.mappers;

import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import enums.AccountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper(componentModel = "spring", imports = {Date.class, AccountStatus.class})
public interface AccountMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new Date())")
    @Mapping(target = "updatedAt", expression = "java(new Date())")
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "status", expression = "java(AccountStatus.OPENED)")
    AccountTable toEntity(AccountRequestDTO dto);
}
