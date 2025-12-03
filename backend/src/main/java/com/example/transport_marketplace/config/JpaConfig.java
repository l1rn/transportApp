package com.example.transport_marketplace.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.transport_marketplace.repo",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableTransactionManagement
public class JpaConfig {

    @Bean(name = "jpaDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource jpaDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:./data/transport_db")
                .username("sa")
                .password("")
                .build();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(jpaDataSource());
        em.setPackagesToScan("com.example.transport_marketplace.model");
        em.setPersistenceUnitName("transportPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.jdbc.lob.non_contextual_creation", "true");

        em.setJpaProperties(props);

        return em;
    }
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager jpaTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}