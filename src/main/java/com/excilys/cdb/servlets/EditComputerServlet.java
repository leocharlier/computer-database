package com.excilys.cdb.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.ComputerService;

public class EditComputerServlet extends HttpServlet {
  public static final String CONF_DAO_FACTORY = "daofactory";
  public static final String VIEW             = "/views/editComputer.jsp";
  
  private ComputerService computerService;
  private ComputerDtoMapper computerDtoMapper;
  
  public void init() throws ServletException {
    this.computerService = new ComputerService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.computerDtoMapper = new ComputerDtoMapper();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
}