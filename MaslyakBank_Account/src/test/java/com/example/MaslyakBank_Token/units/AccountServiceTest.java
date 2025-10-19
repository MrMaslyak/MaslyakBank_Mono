package com.example.MaslyakBank_Token.units;


import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.service.AccountService;
import MaslyakBank_Account.service.CardService;
import MaslyakBank_Account.system.account.AccountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.VerificationUserStatus;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {


    @Mock private  CardDAO cardDAO;
    @Mock private  AccountDAO accountDAO;
    @Mock private  CardService cardService;
    @Mock private  VerificationUserStatus verification;
    @Mock private  AccountFactory accountFactory;

    @InjectMocks
    private AccountService accountService;


    @Test
    void createAccount() {
    }
}
