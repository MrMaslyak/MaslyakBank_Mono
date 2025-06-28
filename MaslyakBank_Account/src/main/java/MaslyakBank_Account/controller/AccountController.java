package MaslyakBank_Account.controller;

import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/accountmanagment/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public AccountTable createAccount(@RequestBody AccountRequestDTO dto) {
        return accountService.createAccount(dto);
    }

}
