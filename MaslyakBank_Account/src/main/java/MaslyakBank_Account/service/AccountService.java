package MaslyakBank_Account.service;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.TransferDTO;
import MaslyakBank_Account.system.account.AccountFactory;
import entity.AccountTable;
import entity.CardTable;
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
    private final AccountDAO accountDAO;
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

    @Transactional
    public void transfer(TransferDTO dto) {
        CardTable toUserCard = cardDAO.getCardByNumber(dto.getToCardNumber());
        CardTable fromUserCard = cardDAO.getCardByNumber(dto.getFromCardNumber());
        AccountTable  toAccount = toUserCard.getAccount();
        AccountTable fromAccount = fromUserCard.getAccount();
        double amount = dto.getAmount();
        double fromBalance = fromAccount.getBalance();
        double toBalance = toAccount.getBalance();
        fromAccount.setBalance(fromBalance - amount);
        toAccount.setBalance(toBalance + amount);
        accountDAO.update(fromAccount);
        accountDAO.update(toAccount);

    }


    public double checkBalance(String cardNumber) {
        CardTable card = cardDAO.getCardByNumber(cardNumber);
        return card.getAccount().getBalance();
    }
}
