package com.excilys.cdb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Lazy
@Configuration
@PropertySource("classpath:properties/hikari.properties")
@ComponentScan({"com.excilys.cdb.service",
				"com.excilys.cdb.persistence",
				"com.excilys.cdb.mapper",
				"com.excilys.cdb.ui",
				"com.excilys.cdb.controller",
				"com.excilys.cdb.config",
				"com.excilys.cdb.validator"})
public class SpringJdbcConfiguration implements WebMvcConfigurer {
	@Value("${driverClassName}")
	String driverClassName;
	@Value("${jdbcUrl}")
	String jdbcUrl;
	@Value("${userDb}")
	String username;
	@Value("${password}")
	String password;
	
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
								.username(username)
								.password(password)
								.url(jdbcUrl)
								.driverClassName(driverClassName)
								.build();
	}
	
	@Bean
    public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbc = new JdbcTemplate();
		jdbc.setDataSource(dataSource());
		return jdbc;
	}
}
