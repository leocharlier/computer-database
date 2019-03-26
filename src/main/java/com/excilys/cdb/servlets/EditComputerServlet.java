package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.mapper.DtoComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class EditComputerServlet extends HttpServlet {
  public static final String CONF_DAO_FACTORY   = "daofactory";
  public static final String VIEW               = "/views/editComputer.jsp";
  public static final String ID_FIELD           = "id";
  public static final String NAME_FIELD         = "computerName";
  public static final String INTRODUCED_FIELD   = "introduced";
  public static final String DISCONTINUED_FIELD = "discontinued";
  public static final String COMPANY_FIELD      = "companyName";
  
  private ComputerService computerService;
  private CompanyService companyService;
  private ComputerDtoMapper computerDtoMapper;
  private DtoComputerMapper dtoComputerMapper;
  private ArrayList<Company> companies;
  
  public void init() throws ServletException {
    this.computerService = new ComputerService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.companyService = new CompanyService(( (DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY) ));
    this.computerDtoMapper = new ComputerDtoMapper();
    this.dtoComputerMapper = new DtoComputerMapper();
    this.companies = companyService.listService();
  }
	    
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	if(request.getParameter("computerId") != null) {
	  int computerId = Integer.parseInt(request.getParameter("computerId"));
	  Optional<Computer> computer = computerService.findService(computerId);
	  if(computer.isPresent()) {
		  Computer computerToEdit = computer.get();
		  ComputerDto computerDto = computerDtoMapper.map(computerToEdit);
		  
		  request.setAttribute("computer", computerDto);
		  request.setAttribute("companies", companies);
	  }
	}
	
	this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
  
  public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
	String updateResult;
	String resultMessage;
	
	String id = request.getParameter(ID_FIELD);
	String computerName = request.getParameter(NAME_FIELD);
	String introduced = request.getParameter(INTRODUCED_FIELD);
	String discontinued = request.getParameter(DISCONTINUED_FIELD);
	String companyName = request.getParameter(COMPANY_FIELD);
	  
	ComputerDto dtoComputer = new ComputerDto(Integer.parseInt(id), computerName, introduced, discontinued, companyName);
	Computer computer = dtoComputerMapper.map(dtoComputer);
	
	try {
      this.computerService.updateService(computer);
      updateResult = "succeed";
      resultMessage = "The computer <strong>" + computerName + "</strong> has been updated !";
    } catch(DaoException e) {
      updateResult = "failed";
      resultMessage = "An SQL error has occured during the update... Please try later.";
    } catch(ComputerNullNameException e) {
      updateResult = "failed";
      resultMessage = "An error has occurred due to the name of the computer... Please change it or try later.";
    } catch(DiscontinuedButNoIntroducedException e) {
      updateResult = "failed";
      resultMessage = "An error has occurred due to the discontinuation and introduction date of the computer... Please change it or try later.";
    } catch(DiscontinuedBeforeIntroducedException e) {
      updateResult = "failed";
      resultMessage = "An error has occurred due to the discontinuation date (it must be after the introduction date)... Please change it or try later.";
    }
    
    request.setAttribute("computer", dtoComputer);
    request.setAttribute("companies", companies);
    request.setAttribute("addResult", updateResult);
    request.setAttribute("resultMessage", resultMessage);
    
    this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
  }
}