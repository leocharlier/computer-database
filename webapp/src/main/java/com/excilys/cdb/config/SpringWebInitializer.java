package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@ComponentScan({"com.excilys.cdb.service",
	"com.excilys.cdb.persistence",
	"com.excilys.cdb.mapper",
	"com.excilys.cdb.ui",
	"com.excilys.cdb.controller",
	"com.excilys.cdb.config",
	"com.excilys.cdb.validator"})
public class SpringWebInitializer implements WebApplicationInitializer {
	public static AnnotationConfigWebApplicationContext ctx;

	@Override
	public void onStartup(final ServletContext servletCtx) throws ServletException {
		ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(HibernateConfiguration.class);
		ctx.setServletContext(servletCtx);
		ctx.refresh();

		servletCtx.addListener(new ContextLoaderListener(ctx));
		DispatcherServlet servlet = new DispatcherServlet(ctx);

		ServletRegistration.Dynamic registration = servletCtx.addServlet("app", servlet);
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}
}
