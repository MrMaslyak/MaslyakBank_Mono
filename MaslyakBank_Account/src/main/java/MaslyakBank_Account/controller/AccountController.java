package MaslyakBank_Account.controller;

import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.service.AccountService;
import MaslyakBank_Account.system.account.AccountSystem;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/accountmanagment/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountSystem accountSystem;

    @PostMapping("/create")
    public AccountTable createAccount(@RequestBody(required = false) AccountRequestDTO dto) {
        accountSystem.isExistAccount(SecurityUtil.getCurrentUser() , dto);
        return accountService.createAccount(dto != null ? dto : new AccountRequestDTO(null));
    }


}
