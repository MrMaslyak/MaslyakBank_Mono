package MaslyakBank_Account.config;

import MaslyakBank_Account.system.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidatorConfig {

    @Bean
    public List<CardValidator> cardValidators() {
        return List.of(
                new CardExistenceValidator(),
                new SameCardValidator(),
                new BalanceValidator(),
                new CardExpirationValidator()
        );
    }
}
