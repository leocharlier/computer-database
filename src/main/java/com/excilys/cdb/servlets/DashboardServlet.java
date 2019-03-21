package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.ComputerService;

public class DashboardServlet extends HttpServlet {
  public static final String CONF_DAO_FACTORY = "daofactory";
  public static final String ATT_USER         = "utilisateur";
  public static final String ATT_FORM         = "form";
  public static final String VUE              = "/WEB-INF/inscription.jsp";
  
  private ComputerService computerService;
  private ComputerDtoMapper computerDtoMapper;
  
  public void init() throws ServletException {
	  this.computerService = new ComputerService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
	  this.computerDtoMapper = new ComputerDtoMapper();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	  ArrayList<Computer> computers = this.computerService.listService();
	  ArrayList<ComputerDto> computerDtos = computerDtoMapper.map(computers);
	  request.setAttribute("computers", computerDtos);
	  request.setAttribute("nbOfComputers", computerDtos.size());
	  this.getServletContext().getRequestDispatcher( "/views/dashboard.jsp" ).forward( request, response );
  }
}