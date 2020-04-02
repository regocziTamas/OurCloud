package com.thomaster.ourcloud.config.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(System.getenv("JDBC_DATABASE_URL"));
        dataSource.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
        dataSource.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));

        return dataSource;
    }
}
