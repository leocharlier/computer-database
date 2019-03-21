package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class EditComputerServlet extends HttpServlet {
  public static final String CONF_DAO_FACTORY = "daofactory";
  public static final String VIEW             = "/views/editComputer.jsp";
  
  private ComputerService computerService;
  private CompanyService companyService;
  private ComputerDtoMapper computerDtoMapper;
  private int computerId;
  
  public void init() throws ServletException {
    this.computerService = new ComputerService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.companyService = new CompanyService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.computerDtoMapper = new ComputerDtoMapper();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	if(request.getParameter("computerId") != null) {
		
	  this.computerId = Integer.parseInt(request.getParameter("computerId"));
	  Optional<Computer> computer = computerService.findService(this.computerId);
	  if(computer.isPresent()) {
		  Computer computerToEdit = computer.get();
		  ComputerDto computerDto = computerDtoMapper.map(computerToEdit);
		  request.setAttribute("computer", computerDto);
		  ArrayList<Company> companies = companyService.listService();
		  request.setAttribute("companies", companies);
	  }
	  
	}
	
	this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
}