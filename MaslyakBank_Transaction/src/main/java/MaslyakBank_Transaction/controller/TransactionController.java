package MaslyakBank_Transaction.controller;

import MaslyakBank_Transaction.dto.TransferCardToCardDTO;
import MaslyakBank_Transaction.dto.TransferSuccessDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("maslyakbank/transactionmanagment/transaction")
public class TransactionController {


    @PostMapping("/transfer/card")
    public TransferSuccessDTO transfer(@RequestBody TransferCardToCardDTO dto) {

        return new TransferSuccessDTO("Transfer successful", true);
    }

}
