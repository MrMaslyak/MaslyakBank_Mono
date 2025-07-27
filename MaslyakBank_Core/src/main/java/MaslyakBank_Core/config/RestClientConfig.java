package MaslyakBank_Core.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Qualifier("tokenRestClient")
    public RestClient tokenServiceRestClient() {
        return RestClient.builder()
                .baseUrl("http://maslyakbank-token:8080/maslyakbank/tokenmanagment/token")
                .build();
    }

    @Bean
    @Qualifier("accountRestClient")
    public RestClient accountServiceRestClient() {
        return RestClient.builder()
                .baseUrl("http://maslyakbank-account:8080/maslyakbank/accountmanagment/account")
                .build();
    }
}
