package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.DaoException;
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
	String addSuccess;
	
	ArrayList<Company> companies = companyService.listService();
    request.setAttribute("companies", companies);
    
	String computerName = request.getParameter(NAME_FIELD);
    String introduced = request.getParameter(INTRODUCED_FIELD);
    String discontinued = request.getParameter(DISCONTINUED_FIELD);
    String companyName = request.getParameter(COMPANY_FIELD);
    
    ComputerDto dtoComputer = new ComputerDto(computerName, introduced, discontinued, companyName);
    Computer computer = dtoComputerMapper.map(dtoComputer);
    
    try {
      this.computerService.createService(computer);
      addSuccess = "succeed";
    } catch(DaoException e) {
      addSuccess = "failed";
    }
    
    request.setAttribute("addResult", addSuccess);
    request.setAttribute("computerName", computerName);
    
    this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
}