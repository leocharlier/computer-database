package com.excilys.cdb.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

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
	public SessionFactory getSessionFactory() {
	    StandardServiceRegistryBuilder registryBuilder = 
	          new StandardServiceRegistryBuilder();
	
	    Map<String, Object> settings = new HashMap<>();
	    settings.put(Environment.DRIVER, driverClassName);
	    settings.put(Environment.URL, jdbcUrl);
	    settings.put(Environment.USER, username);
	    settings.put(Environment.PASS, password);
	
	    registryBuilder.applySettings(settings);
	
	    StandardServiceRegistry registry = registryBuilder.build();
	    MetadataSources sources = new MetadataSources(registry)
	          .addAnnotatedClass(Computer.class)
	          .addAnnotatedClass(Company.class);
	    Metadata metadata = sources.getMetadataBuilder().build();
	    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
	    return sessionFactory;
   }
	
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
