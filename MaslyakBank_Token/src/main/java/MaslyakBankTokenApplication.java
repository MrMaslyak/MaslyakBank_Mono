import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {
		"MaslyakBank_Token",
		"MaslyakBank_Model",
		"MaslyakBank_SecurityKernel",
		"util",
		"service",
		"dao",
		"dto",
		"system",
		"config",
		"filter",
		"mappers",
		"controller",
		"enums"
})
@PropertySource("classpath:/application_token.properties")
@EntityScan({"entity", "entity"})
public class MaslyakBankTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaslyakBankTokenApplication.class, args);
	}

}
