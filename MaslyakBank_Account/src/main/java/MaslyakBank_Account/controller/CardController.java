package MaslyakBank_Account.controller;

import MaslyakBank_Account.dao.AccountDAO;
import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.entity.AccountTable;
import MaslyakBank_Account.entity.CardTable;
import MaslyakBank_Account.service.CardService;
import MaslyakBank_Account.system.account.AccountSystem;
import entity.UsersTable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maslyakbank/accountmanagment/card")
public class CardController {

    private final CardService cardService;
    private final CardDAO cardDAO;

    @PostMapping("/create")
    public CardTable createCard(@RequestBody CardRequestDTO dto) {
        UsersTable user = SecurityUtil.getCurrentUser();
        CardTable card = cardService.createCard(user, dto);
        return cardDAO.saveCard(card);
    }

}
