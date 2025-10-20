package com.example.MaslyakBank_Token.units;

import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.dto.CardValidationResultDTO;
import MaslyakBank_Account.service.CardService;
import MaslyakBank_Account.system.account.AccountSystem;
import MaslyakBank_Account.system.builder.CardBuilder;
import MaslyakBank_Account.system.validators.CardValidator;
import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.AccountTable;
import entity.CardTable;
import entity.UsersTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import util.SecurityUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock private  CardBuilder cardBuilder;
    @Mock private  AccountSystem accountSystem;
    @Mock private  CardDAO cardDAO;
    @Mock private  List<CardValidator> cardValidators;
    @Mock private CardValidator validator;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardValidators = List.of(validator);
        cardService.setCardValidators(cardValidators);
    }


    @Test
    void createCard_ensureAccount() {
        // arrange
        UsersTable user = new UsersTable();
        user.setId(UUID.randomUUID());
        CardRequestDTO dto = new CardRequestDTO();
        CardBuilder builder = mock(CardBuilder.class);
        AccountTable account = new AccountTable();
        account.setAccountNumber("1234567890");

        when(accountSystem.ensureAccount(user.getId(), dto)).thenReturn(account);

        CardTable expectedCard = new CardTable();
        expectedCard.setAccount(account);
        expectedCard.setCardNumber("987654321");

        // настраиваем цепочку билдера
        when(cardBuilder.newCard()).thenReturn(builder);
        when(builder.withAccount(account)).thenReturn(builder);
        when(builder.card(dto)).thenReturn(builder);
        when(builder.build()).thenReturn(expectedCard);

        // act
        CardTable card = cardService.createCard(user, dto);

        // assert
        assertThat(card).isEqualTo(expectedCard);

        verify(cardBuilder).newCard();
        verify(builder).withAccount(account);
        verify(builder).card(dto);
        verify(builder).build();
    }


    @Test
    void createCard_build() {
        // arrange
        AccountTable account = new AccountTable();
        account.setAccountNumber("123456");

        CardRequestDTO dto = new CardRequestDTO();

        CardTable expectedCard = new CardTable();
        expectedCard.setAccount(account);
        expectedCard.setCardNumber("987654321");

        // настраиваем цепочку билдера
        when(cardBuilder.newCard()).thenReturn(cardBuilder);
        when(cardBuilder.withAccount(account)).thenReturn(cardBuilder);
        when(cardBuilder.card(dto)).thenReturn(cardBuilder);
        when(cardBuilder.build()).thenReturn(expectedCard);

        // act
        CardTable result = cardService.createCard(account, dto);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo(account);
        assertThat(result.getCardNumber()).isEqualTo("987654321");
        verify(cardBuilder).newCard();
        verify(cardBuilder).withAccount(account);
        verify(cardBuilder).card(dto);
        verify(cardBuilder).build();
    }

    @Test
    void validateCard_positive(){
        // arrange
        String fromCardNumber = "1234567890123456";
        String toCardNumber = "9876543210987654";

        UsersTable user = new UsersTable();
        user.setId(UUID.randomUUID());

        AccountTable account = new AccountTable();
        account.setUser(user);

        CardTable fromCard = new CardTable();
        fromCard.setCardNumber(fromCardNumber);
        fromCard.setAccount(account);

        CardTable toCard = new CardTable();
        toCard.setCardNumber(toCardNumber);


        when(cardDAO.getCardByNumber(fromCardNumber)).thenReturn(fromCard);
        when(cardDAO.getCardByNumber(toCardNumber)).thenReturn(toCard);


        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(user);

            //act
            CardValidationResultDTO result = cardService.validateCard(fromCardNumber, toCardNumber);

            //assert
            assertThat(result).isNotNull();
            assertThat(result.getFromCard()).isEqualTo(fromCard);
            assertThat(result.getToCard()).isEqualTo(toCard);
            for (CardValidator validator : cardValidators) {
                verify(validator).validate(fromCard, toCard);
            }
            verify(cardDAO).getCardByNumber(fromCardNumber);
            verify(cardDAO).getCardByNumber(toCardNumber);
            verifyNoMoreInteractions(cardDAO);
        }
    }

    @Test
    void validateCard_forbidden() {
        // arrange
        String fromCardNumber = "1234567890123456";
        String toCardNumber = "9876543210987654";

        UsersTable currentUser = new UsersTable();
        currentUser.setId(UUID.randomUUID());

        UsersTable cardOwner = new UsersTable();
        cardOwner.setId(UUID.randomUUID());

        AccountTable account = new AccountTable();
        account.setUser(cardOwner);

        CardTable fromCard = new CardTable();
        fromCard.setCardNumber(fromCardNumber);
        fromCard.setAccount(account);

        CardTable toCard = new CardTable();
        toCard.setCardNumber(toCardNumber);

        when(cardDAO.getCardByNumber(fromCardNumber)).thenReturn(fromCard);
        when(cardDAO.getCardByNumber(toCardNumber)).thenReturn(toCard);

        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUser).thenReturn(currentUser);

            // act & assert
            TransactionException exception = assertThrows(TransactionException.class,
                    () -> cardService.validateCard(fromCardNumber, toCardNumber));

            assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(exception.getMessage()).isEqualTo("This card does not belong to the current user");
        }
    }



}
