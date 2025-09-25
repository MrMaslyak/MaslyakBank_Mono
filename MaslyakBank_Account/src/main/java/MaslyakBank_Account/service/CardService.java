package MaslyakBank_Account.service;


import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.dto.CardValidationResultDTO;
import MaslyakBank_Account.system.account.AccountSystem;
import MaslyakBank_Account.system.builder.CardBuilder;
import MaslyakBank_Account.system.validators.CardValidator;
import MaslyakBank_Account.system.validators.exception.TransactionException;
import entity.AccountTable;
import entity.CardTable;
import entity.UsersTable;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import util.SecurityUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class CardService {

    private final CardBuilder cardBuilder;
    private final AccountSystem accountSystem;
    private final CardDAO cardDAO;
    private final List<CardValidator> cardValidators;


    public CardTable createCard(UsersTable user, CardRequestDTO dto) {
        AccountTable account = accountSystem.ensureAccount(user.getId(), dto);
        return createCard(account, dto);
    }


    public CardTable createCard(AccountTable account, @Nullable CardRequestDTO dto) {
        return   cardBuilder
                .newCard()
                .withAccount(account)
                .card(dto)
                .build();
    }


    public CardValidationResultDTO validateCard(String fromCardNumber, String toCardNumber) {
        UsersTable user = SecurityUtil.getCurrentUser();
        CardTable fromCard = cardDAO.getCardByNumber(fromCardNumber);
        CardTable toCard = cardDAO.getCardByNumber(toCardNumber);

        if (!fromCard.getAccount().getUser().getId().equals(user.getId())) {
            throw new TransactionException(HttpStatus.FORBIDDEN, "This card does not belong to the current user");
        }
        for (CardValidator validator : cardValidators) {
            validator.validate(fromCard, toCard);
        }
        return new CardValidationResultDTO(fromCard, toCard);
    }
}
