package MaslyakBank_Transaction.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application_transaction.properties")
public class HibernateConfig {

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USER;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String DDL_AUTO;

    @Value("${spring.jpa.show-sql}")
    private String SHOW_SQL;

    @Value("${spring.jpa.database-platform}")
    private String DIALECT;


    @Bean
    public DataSource datasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("MaslyakBank_Transaction.entity");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaProperties(getHibernateProperties());
        return factory;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect",DIALECT);
        properties.setProperty("hibernate.show_sql", SHOW_SQL);
        properties.setProperty("hibernate.hbm2ddl.auto",DDL_AUTO);
        return properties;
    }

}
