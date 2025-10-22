package MaslyakBank_Token.units;


import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.TransferDTO;
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

    @Test
    void checkBalance(){
        // arrange
        String cardNumber = "1234567890123456";
        CardTable cardTable = new CardTable();
        AccountTable accountTable = new AccountTable();
        accountTable.setBalance(1000.0);
        cardTable.setAccount(accountTable);

        when(cardDAO.getCardByNumber(cardNumber)).thenReturn(cardTable);

        // act
        double result = accountService.checkBalance(cardNumber);

        // assert
        verify(cardDAO, times(1)).getCardByNumber(cardNumber);
        assertThat(result).isEqualTo(1000.0);
        assertThat(result).isNotNegative();
    }

    @Test
    void transfer_positive() {
        // arrange
        TransferDTO transferDTO = new TransferDTO(
                "1111222233334444",
                "5555666677778888",
                100.0,
                "test"
        );

        CardTable fromUserCard = new CardTable();
        CardTable toUserCard = new CardTable();

        AccountTable fromAccount = new AccountTable();
        AccountTable toAccount = new AccountTable();

        fromAccount.setBalance(500.0);
        toAccount.setBalance(200.0);

        fromUserCard.setAccount(fromAccount);
        toUserCard.setAccount(toAccount);

        when(cardDAO.getCardByNumber(transferDTO.getFromCardNumber())).thenReturn(fromUserCard);
        when(cardDAO.getCardByNumber(transferDTO.getToCardNumber())).thenReturn(toUserCard);

        // act
        accountService.transfer(transferDTO);

        // assert
        verify(cardDAO, times(1)).getCardByNumber(transferDTO.getFromCardNumber());
        verify(cardDAO, times(1)).getCardByNumber(transferDTO.getToCardNumber());
        verify(accountDAO, times(1)).update(fromAccount);
        verify(accountDAO, times(1)).update(toAccount);


        assertThat(fromAccount.getBalance()).isEqualTo(400.0);
        assertThat(toAccount.getBalance()).isEqualTo(300.0);
    }

    @Test
    void transfer_fromBalanceUnderAmount() {
        // arrange
        TransferDTO transferDTO = new TransferDTO(
                "1111222233334444",
                "5555666677778888",
                1000.0,
                "test"
        );

        CardTable fromUserCard = new CardTable();
        CardTable toUserCard = new CardTable();

        AccountTable fromAccount = new AccountTable();
        AccountTable toAccount = new AccountTable();

        fromAccount.setBalance(500.0);
        toAccount.setBalance(200.0);

        fromUserCard.setAccount(fromAccount);
        toUserCard.setAccount(toAccount);

        when(cardDAO.getCardByNumber(transferDTO.getFromCardNumber())).thenReturn(fromUserCard);
        when(cardDAO.getCardByNumber(transferDTO.getToCardNumber())).thenReturn(toUserCard);

        // act + assert
        assertThatThrownBy(() -> accountService.transfer(transferDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Недостаточно средств для перевода");

        // проверяем, что обновления не было
        verify(accountDAO, never()).update(any());
    }

    @Test
    void transfer_toCardEqualFromCard() {
        // arrange
        TransferDTO transferDTO = new TransferDTO(
                "5555666677778888",
                "5555666677778888",
                100,
                "test"
        );

        CardTable fromUserCard = new CardTable();
        CardTable toUserCard = new CardTable();

        AccountTable fromAccount = new AccountTable();
        AccountTable toAccount = new AccountTable();

        fromAccount.setBalance(500.0);
        toAccount.setBalance(200.0);

        fromUserCard.setAccount(fromAccount);
        toUserCard.setAccount(toAccount);

        when(cardDAO.getCardByNumber(transferDTO.getFromCardNumber())).thenReturn(fromUserCard);
        when(cardDAO.getCardByNumber(transferDTO.getToCardNumber())).thenReturn(toUserCard);

        // act + assert
        assertThatThrownBy(() -> accountService.transfer(transferDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Перевод на ту же карту невозможен");

        // проверяем, что обновления не было
        verify(accountDAO, never()).update(any());
    }



}
