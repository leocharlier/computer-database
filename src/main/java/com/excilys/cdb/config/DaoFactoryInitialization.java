package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.excilys.cdb.persistence.DaoFactory;

public class DaoFactoryInitialization implements ServletContextListener {
	private static final String ATT_DAO_FACTORY = "daofactory";
	private DaoFactory daoFactory;

	@Override
	public void contextDestroyed(ServletContextEvent event) {}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		this.daoFactory = DaoFactory.getInstance();
		servletContext.setAttribute(ATT_DAO_FACTORY, this.daoFactory);
	}

}
