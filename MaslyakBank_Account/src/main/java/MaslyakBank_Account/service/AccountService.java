package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.AccountRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.mappers.AccountMapper;
import MaslyakBank_Account.system.util.SecurityUtil;
import dao.UserDAO;
import dto.TokenRequestDTO;
import entity.UsersTable;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import system.VerificationUserStatus;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;
    private final UserDAO userDAO;
    private final CardService cardService;
    private final VerificationUserStatus  verification;
    private RestClient tokenRestClient;


    @Transactional
    public AccountTable createAccount(AccountRequestDTO dto) {
        AccountTable account = accountMapper.toEntity(dto);
        UsersTable user = SecurityUtil.getCurrentUser();
        account.setUser(user);


        verification.checkStatus(user);

        AccountTable savedAccount = accountDAO.saveAccount(account);
        createCard(savedAccount);
        userDAO.updateUser(user);

        return savedAccount;
    }

    private void createCard(AccountTable account) {
        CardTable cardDefault = cardService.createDefaultCard(account);
        cardDAO.createCard(cardDefault);
    }

    private UsersTable validationToken(String token) {
        try {
            TokenRequestDTO dto = tokenRestClient.post()
                    .uri("/validation")
                    .header("Maslyak-Token", token)
                    .retrieve()
                    .body(TokenRequestDTO.class);

            assert dto != null;
            return userDAO.findById(dto.getUserId());

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Token validation failed: " + ex.getResponseBodyAsString());
        }
    }
}
