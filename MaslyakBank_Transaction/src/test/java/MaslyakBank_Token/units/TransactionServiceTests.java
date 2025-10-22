package MaslyakBank_Token.units;

import MaslyakBank_Transaction.dao.DetailsDAO;
import MaslyakBank_Transaction.dao.TransactionDAO;
import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.entity.TransactionDetailsTable;
import MaslyakBank_Transaction.entity.TransactionTable;
import MaslyakBank_Transaction.service.TransactionService;
import MaslyakBank_Transaction.system.DetailsBuilder;
import MaslyakBank_Transaction.system.TransactionBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestClient;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock private  TransactionBuilder transactionBuilder;
    @Mock private  DetailsBuilder detailsBuilder;
    @Mock private  TransactionDAO transactionDAO;
    @Mock private  DetailsDAO detailsDAO;
    @Mock private  RestClient cardManagmentService;
    @Mock  private  RestClient accountManagmentService;
    @Mock  private  RedisTemplate<String, TransactionTable> transactionRedisTemplate;
    @Mock  private  RedisTemplate<String, TransactionDetailsTable> detailsRedisTemplate;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transferCardToCard(){
        //arrange
        TransferDTO dto = new TransferDTO(
                "1234567890",
                "0987654321",
                200,
                "Tests"
        );
        TransactionTable transaction = null;
    }
}
