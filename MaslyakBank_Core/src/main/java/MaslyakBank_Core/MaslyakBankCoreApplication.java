package MaslyakBank_Core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {
		"MaslyakBank_Core",
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
		"enums"
})
@PropertySource("classpath:/application_core.properties")
@EntityScan({"MaslyakBank_Core.entity", "entity"})
public class MaslyakBankCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaslyakBankCoreApplication.class, args);
	}

}
