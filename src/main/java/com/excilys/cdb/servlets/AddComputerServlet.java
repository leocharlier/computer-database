package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.mapper.DtoComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class AddComputerServlet extends HttpServlet {
  public static final String CONF_DAO_FACTORY   = "daofactory";
  public static final String VIEW               = "/views/addComputer.jsp";
  public static final String NAME_FIELD         = "computerName";
  public static final String INTRODUCED_FIELD   = "introduced";
  public static final String DISCONTINUED_FIELD = "discontinued";
  public static final String COMPANY_FIELD      = "companyName";
  public static final String ATT_ERRORS         = "errors";
  
  private ComputerService computerService;
  private CompanyService companyService;
  private DtoComputerMapper dtoComputerMapper;
  
  public void init() throws ServletException {
    this.computerService = new ComputerService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.companyService = new CompanyService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.dtoComputerMapper = new DtoComputerMapper();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    ArrayList<Company> companies = companyService.listService();
    request.setAttribute("companies", companies);
	this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
  
  public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	String result;
    Map<String, String> errors = new HashMap<String, String>();
	
	String computerName = request.getParameter(NAME_FIELD);
    String introduced = request.getParameter(INTRODUCED_FIELD);
    String discontinued = request.getParameter(DISCONTINUED_FIELD);
    String companyName = request.getParameter(COMPANY_FIELD);
    
    try {
    	computerNameValidation(computerName);
    } catch (ComputerNullNameException e) {
    	errors.put(NAME_FIELD, e.getMessage());
    }
//    
//    ComputerDto dtoComputer = new ComputerDto(computerName, introduced, discontinued, companyName);
//    
//    System.out.println(introduced + discontinued);
//    Computer computer = dtoComputerMapper.map(dtoComputer);
//    
//    this.computerService.createService(computer);
    this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }

  private void computerNameValidation( String name ) throws ComputerNullNameException {
	  if(name.trim().isEmpty()) {
		  throw new ComputerNullNameException("Computer name must be set.");
	  }
  }
}