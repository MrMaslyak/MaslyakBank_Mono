package MaslyakBank_Transaction.controller;

import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.dto.TransferSuccessDTO;
import MaslyakBank_Transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("maslyakbank/transactionmanagment/transaction")
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/transfer/card")
    public TransferSuccessDTO transferCard(@RequestBody TransferDTO dto) {
        transactionService.transferCardToCard(dto);
        return new TransferSuccessDTO("Transfer successful", true);
    }

}
