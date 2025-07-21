package MaslyakBank_Account.mappers;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.enums.AccountType;
import enums.AccountStatus;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper(componentModel = "spring", imports = {Date.class, AccountStatus.class, AccountType.class})
public interface AccountMapper {


}
