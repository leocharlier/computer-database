package com.excilys.cdb.config;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import com.excilys.cdb.model.Authority;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.User;

@Lazy
@Configuration
@PropertySource("classpath:hibernate.properties")
@ComponentScan({"com.excilys.cdb.service",
				"com.excilys.cdb.persistence",
				"com.excilys.cdb.mapper",
				"com.excilys.cdb.ui",
				"com.excilys.cdb.controller",
				"com.excilys.cdb.config",
				"com.excilys.cdb.validator"})
public class HibernateConfiguration {
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
	          .addAnnotatedClass(Company.class)
	          .addAnnotatedClass(Authority.class)
	          .addAnnotatedClass(User.class);
	    Metadata metadata = sources.getMetadataBuilder().build();
	    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
	    return sessionFactory;
   }
}
