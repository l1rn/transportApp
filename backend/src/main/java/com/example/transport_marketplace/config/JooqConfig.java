package com.example.transport_marketplace.config;

import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.conf.ParamType;
import org.jooq.conf.StatementType;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableCaching
public class JooqConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSourceProxy(DataSource dataSource){
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(
            TransactionAwareDataSourceProxy dataSourceProxy
    ) {
        return new DataSourceConnectionProvider(dataSourceProxy);
    }

    @Bean
    public DefaultDSLContext dsl(org.jooq.Configuration configuration){
        return new DefaultDSLContext(configuration);
    }

    @Bean
    public org.jooq.Configuration jooqConfiguration(
            ConnectionProvider connectionProvider,
            TransactionProvider transactionProvider
    ){
        DefaultConfiguration configuration = new DefaultConfiguration();
        configuration.set(connectionProvider);
        configuration.set(transactionProvider);
        configuration.set(SQLDialect.POSTGRES);


        configuration.settings()
                .withExecuteLogging(true)
                .withRenderFormatted(true)
                .withRenderSchema(false)
                .withStatementType(StatementType.PREPARED_STATEMENT)
                .withFetchSize(100)
                .withMaxRows(1000);

        configuration = (DefaultConfiguration) configuration.deriveSettings(
                s -> s.withExecuteLogging(false)
                        .withRenderFormatted(true)
                        .withRenderSchema(false)
                        .withStatementType(StatementType.PREPARED_STATEMENT)
                        .withFetchSize(100)
                        .withMaxRows(1000)
                        .withParamType(ParamType.INDEXED)
                        .withExecuteWithOptimisticLocking(true)
        );

        return configuration;
    }
}
