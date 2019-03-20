package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;

public class Dashboard extends HttpServlet {
  public static final String CONF_DAO_FACTORY = "daofactory";
  public static final String ATT_USER         = "utilisateur";
  public static final String ATT_FORM         = "form";
  public static final String VUE              = "/WEB-INF/inscription.jsp";
  
  private ComputerDao computerDao;
  
  public void init() throws ServletException {
	  this.computerDao = ( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ).getComputerDao();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	  ArrayList<Computer> computers = this.computerDao.list();
	  request.setAttribute("computers", computers);
	  this.getServletContext().getRequestDispatcher( "/views/dashboard.jsp" ).forward( request, response );
  }
}