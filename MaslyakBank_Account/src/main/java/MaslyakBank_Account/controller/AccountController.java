package MaslyakBank_Account.controller;

import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/accountmanagment/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public AccountTable createAccount() {
        return accountService.createDefaultAccount();
    }

}
