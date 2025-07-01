package MaslyakBank_Core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient tokenServiceRestClient() {
        return RestClient.builder()
                .baseUrl("http://maslyakbank-token:8080/maslyakbank/tokenmanagment/token")
                .build();
    }
}
