package com.example.MaslyakBank_Token.units;


import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.service.AccountService;
import MaslyakBank_Account.service.CardService;
import MaslyakBank_Account.system.account.AccountFactory;
import entity.AccountTable;
import entity.CardTable;
import entity.UsersTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import system.VerificationUserStatus;
import util.SecurityUtil;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

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
    void createDefaultAccount() {
        //arrange
        UsersTable user = new UsersTable();
        AccountTable accountTable = new AccountTable();
        CardTable cardTable = new CardTable();

        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(user);

            doNothing().when(verification).checkStatus(user);
            when(accountFactory.createAccountFor(user)).thenReturn(accountTable);
            when(cardService.createCard(accountTable, null)).thenReturn(cardTable);
            when(cardDAO.saveCard(cardTable)).thenReturn(cardTable);

            // act
            AccountTable result = accountService.createDefaultAccount();

            // assert
            verify(verification, times(1)).checkStatus(user);
            verify(accountFactory, times(1)).createAccountFor(user);
            verify(cardService, times(1)).createCard(accountTable, null);
            verify(cardDAO, times(1)).saveCard(cardTable);
            verifyNoInteractions(accountDAO);

            assertThat(result).isEqualTo(accountTable);
        }

    }
}
