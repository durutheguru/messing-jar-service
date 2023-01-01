package com.julianduru.messingjarservice.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * created by julian on 05/11/2022
 */
@TestConfiguration
public class OAuthServiceDatabaseConfig {


    @Bean
    public DataSource oauthServiceDataSource(
        @Value("${test.code.config.oauth-service.datasource.url}") String url,
        @Value("${test.code.config.oauth-service.datasource.username}") String username,
        @Value("${test.code.config.oauth-service.datasource.password}") String password
    ) {
        var datasource = new MysqlDataSource();

        datasource.setUser(username);
        datasource.setPassword(password);
        datasource.setUrl(url);

        return datasource;
    }


    @Bean
    public JdbcTemplate oauthServiceJdbcTemplate(DataSource oauthServiceDataSource) {
        return new JdbcTemplate(oauthServiceDataSource);
    }


}

