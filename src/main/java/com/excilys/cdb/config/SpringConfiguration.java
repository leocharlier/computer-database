package com.excilys.cdb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Lazy
@Configuration
@PropertySource("classpath:properties/hikari.properties")
@ComponentScan({"com.excilys.cdb.service",
				"com.excilys.cdb.persistence",
				"com.excilys.cdb.mapper",
				"com.excilys.cdb.ui"})
public class SpringConfiguration {
	@Value("${driverClassName}")
	String driverClassName;
	@Value("${jdbcUrl}")
	String jdbcUrl;
	@Value("${userDb}")
	String username;
	@Value("${password}")
	String password;
	
	@Bean
	public DataSource getDataSource() {
		return DataSourceBuilder.create()
								.username(username)
								.password(password)
								.url(jdbcUrl)
								.driverClassName(driverClassName)
								.build();
	}
}
