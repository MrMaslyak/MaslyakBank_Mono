package MaslyakBank_Account.mappers;

import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.enums.AccountType;
import enums.AccountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper(componentModel = "spring", imports = {Date.class, AccountStatus.class, AccountType.class})
public interface AccountMapper {

    @Mapping(target = "user", ignore = true)
    AccountTable toEntity(AccountRequestDTO dto);
}
