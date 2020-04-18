package main.restservice.config;

import main.restservice.controller.TinderController;
import main.restservice.dao.DbProfileDao;
import main.restservice.service.DefaultProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TinderConfig {
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("main.restservice.domain");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(hibernateProperties());
        return factoryBean;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:hsqldb:file:C:/Users/rssvl/Desktop/homework/hsqldb-2.5.0/hsqldb/bin/mydb.tmp/Users");
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty("hibernate.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        return hibernateProperties;
    }


    @Bean
    public TinderController tinderController() {
        return new TinderController(defaultProfileService());
    }

    @Bean
    public DbProfileDao dbProfileDao() {
        return new DbProfileDao();
    }

    @Bean
    public DefaultProfileService defaultProfileService() {
        return new DefaultProfileService(dbProfileDao());
    }
}
