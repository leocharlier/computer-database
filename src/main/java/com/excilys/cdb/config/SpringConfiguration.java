package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Lazy
@Configuration
@ComponentScan({"com.excilys.cdb.service",
				"com.excilys.cdb.persistence",
				"com.excilys.cdb.mapper",
				"com.excilys.cdb.ui"})
public class SpringConfiguration {
	private static final String FICHIER_PROPERTIES = "/properties/hikari.properties";
	
	@Bean
	public HikariDataSource getDataSource() {
		return new HikariDataSource(new HikariConfig(FICHIER_PROPERTIES));
	}
}
