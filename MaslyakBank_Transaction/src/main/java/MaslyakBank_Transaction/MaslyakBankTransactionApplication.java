package MaslyakBank_Transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@PropertySource("classpath:/application_transaction.properties")
@EntityScan({"MaslyakBank_Transaction.entity", "entity", "MaslyakBank_Account.entity"})
@EnableScheduling
public class MaslyakBankTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaslyakBankTransactionApplication.class, args);
	}

}
