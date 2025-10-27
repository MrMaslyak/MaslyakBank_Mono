package MaslyakBank_Transaction.integrations;

import MaslyakBank_Transaction.dto.TransferDTO;
import MaslyakBank_Transaction.service.TransactionService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import dao.UserDAO;
import entity.UsersTable;
import enums.UserRole;
import enums.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TransactionServiceTest {

    @Autowired UserDAO userDAO;
    @Autowired TransactionService transactionService;
    @MockitoBean
    private  RedisTemplate<String, Object> redisTemplate;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void transferCardToCard(){
        //arrange
        UsersTable user_debit = new UsersTable();
        user_debit.setLogin("ivan");
        user_debit.setPasswordSalt("encodedPass");
        user_debit.setStatus(UserStatus.COMPLETED);
        user_debit.setRole(UserRole.USER);
        userDAO.saveUser(user_debit);

        UsersTable user_credit = new UsersTable();
        user_credit.setLogin("petya");
        user_credit.setPasswordSalt("encodedPass");
        user_credit.setStatus(UserStatus.COMPLETED);
        user_credit.setRole(UserRole.USER);
        userDAO.saveUser(user_credit);

        // Создаем счета и карты для дебета и кредита
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/maslyakbank/accountmanagment/account/create"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("""
                        {"id":1,"balance":1000.0,"currency":"UAH"}
                        """)));

        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/maslyakbank/accountmanagment/card/create"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("""
                        {"id":1,"number":"1111-2222-3333-4444","account":{"id":1,"balance":1000.0}}
                        """)));

        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/maslyakbank/accountmanagment/card/validate"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("""
                        {"fromCard":{"account":{"balance":1000.0,"currency":"UAH"}},
                         "toCard":{"account":{"balance":500.0,"currency":"UAH"}}}
                        """)));

        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathEqualTo("/maslyakbank/accountmanagment/account/balance"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("1000.0")));

        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathEqualTo("/maslyakbank/accountmanagment/account/transfer/card"))
                .willReturn(WireMock.aResponse().withStatus(200)));

        ReflectionTestUtils.setField(transactionService, "redisTemplate", Mockito.mock(RedisTemplate.class));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ReflectionTestUtils.setField(transactionService, "accountRestClient", restTemplate);
        ReflectionTestUtils.setField(transactionService, "cardRestClient", restTemplate);


        TransferDTO dto = new TransferDTO(
                "1111-2222-3333-4444", "5555-6666-7777-8888", 100, "test"
        );


        transactionService.transferCardToCard(dto);


    }
}
