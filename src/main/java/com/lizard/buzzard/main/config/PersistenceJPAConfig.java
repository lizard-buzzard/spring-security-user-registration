package com.lizard.buzzard.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Source for @Bean's definition used in Persistence JPA Repository
 */
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence.properties" })
@ComponentScan({ "com.lizard.buzzard.persistence" })
@EnableJpaRepositories(basePackages = "com.lizard.buzzard.persistence.dao")
public class PersistenceJPAConfig {

    @Autowired
    private Environment env;

    public PersistenceJPAConfig() {
        super();
    }

    /**
     * @return  LocalContainerEntityManagerFactoryBean - the bean is a factory for container's entities
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.lizard.buzzard.persistence.model" });
        em.setJpaProperties(additionalProperties());
        return em;
    }

    /**
     * Jdbc datasource object, used for LocalContainerEntityManagerFactoryBean
     * @return  DataSource
     */
    @Bean
    public  DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        return dataSource;
    }

    /**
     * Transaction Manager for Jpa model entities, use LocalContainerEntityManagerFactoryBean
     * @return  JpaTransactionManager
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Get properties from persistence.properties
     * @return
     */
    protected Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        return hibernateProperties;
    }

}
