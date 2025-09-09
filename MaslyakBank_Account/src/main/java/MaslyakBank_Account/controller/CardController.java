package MaslyakBank_Account.controller;

import MaslyakBank_Account.dao.CardDAO;
import MaslyakBank_Account.dto.CardRequestDTO;
import MaslyakBank_Account.service.CardService;
import entity.CardTable;
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

    @GetMapping("/validate")
    public boolean checkCard(@RequestParam String fromCardNumber,
                             @RequestParam String toCardNumber) {
        return cardService.validateCard(fromCardNumber, toCardNumber);
    }

}
