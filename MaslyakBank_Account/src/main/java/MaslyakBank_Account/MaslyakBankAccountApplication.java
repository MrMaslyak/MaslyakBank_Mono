package MaslyakBank_Account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {
		"MaslyakBank_Account",
		"MaslyakBank_Model",
		"MaslyakBank_SecurityKernel",
		"details",
		"util",
		"service",
		"system",
		"dto",
		"filter",
		"dao",
		"controller",
		"repository",
		"entity",
		"enums"
})
@PropertySource("classpath:/application_account.properties")
@EntityScan({"MaslyakBank_Account.entity", "entity"})
public class MaslyakBankAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaslyakBankAccountApplication.class, args);
	}

}
