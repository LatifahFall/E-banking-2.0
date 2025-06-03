package org.latifah.employeedashboardback.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.latifah.employeedashboardback.ai.LibreTranslateClient;
import org.latifah.employeedashboardback.aspect.AuditAspect;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan({
        "org.latifah.employeedashboardback.config",
        "org.latifah.employeedashboardback.service",
        "org.latifah.employeedashboardback.filter",
        "org.latifah.employeedashboardback.controller",
        "org.latifah.employeedashboardback",
        "org.latifah.employeedashboardback.aspect"
})
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.latifah.employeedashboardback.repository")
public class AppConfig {

    // === Web / utilitaire Beans ===
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }

    @Bean
    public LibreTranslateClient libreTranslateClient() {
        return new LibreTranslateClient();
    }

//    // === Persistence Beans ===
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("org.postgresql.Driver");
//        ds.setUrl("jdbc:postgresql://localhost:5433/employeedashdb");
//        ds.setUsername("postgres");
//        ds.setPassword("root");
//        return ds;
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
//        emfb.setDataSource(dataSource());
//        emfb.setPackagesToScan("org.latifah.employeedashboardback.entity");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        emfb.setJpaVendorAdapter(vendorAdapter);
//
//        Properties jpaProperties = new Properties();
//        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
//        jpaProperties.put("hibernate.show_sql", "true");
//        jpaProperties.put("hibernate.format_sql", "true");
//
//        emfb.setJpaProperties(jpaProperties);
//        return emfb;
//    }

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager tm = new JpaTransactionManager();
//        tm.setEntityManagerFactory(entityManagerFactory().getObject());
//        return tm;
//    }

//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
}
