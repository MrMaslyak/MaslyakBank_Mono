package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.system.account.AccountFactory;
import entity.UsersTable;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.VerificationUserStatus;
import util.SecurityUtil;

@Service
@AllArgsConstructor
public class AccountService {

    private final CardDAO cardDAO;
    private final CardService cardService;
    private final VerificationUserStatus  verification;
    private final AccountFactory accountFactory;


    @Transactional
    public AccountTable createDefaultAccount() {
        UsersTable user = SecurityUtil.getCurrentUser();
        verification.checkStatus(user);

        AccountTable account = accountFactory.createAccountFor(user);
        CardTable card = cardService.createCard(account, null);
        cardDAO.saveCard(card);

        return account;
    }



}
