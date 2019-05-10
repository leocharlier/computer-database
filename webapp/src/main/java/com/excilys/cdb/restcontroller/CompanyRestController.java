package com.excilys.cdb.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;

@RestController
@RequestMapping(path="/api/companies", produces = "application/json")
public class CompanyRestController {
	CompanyService companyService;
	
	public CompanyRestController(CompanyService sCompany) {
		companyService = sCompany;
	}

	@GetMapping
	public List<Company> getPageableCompanies(@RequestParam(name = "size", defaultValue = "10") String size,
			@RequestParam(name = "page", defaultValue = "1") String page) 
					throws ResponseStatusException {
		try {
			ArrayList<Company> companies = companyService.listService();
			
			if(!companies.isEmpty()) {
				Page<Company >currentPage = new Page<Company>(companies, Integer.parseInt(size));
				int numPage = Integer.parseInt(page);
				if(numPage < 1) {
					numPage = 1;
				} else if(numPage > currentPage.getMaxPages()) {
					numPage = currentPage.getMaxPages();
				}
					
				return currentPage.getPageData(numPage - 1);
			} else {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT , "Company's list is empty.");
			}
		} catch(DaoException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "List Companies : An error has occured during the listing...", e);
		}
		
	}
	
	@DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompany(@PathVariable("id") int id) throws ResponseStatusException {
		try {
			Optional<Company> companyToDelete = companyService.findById(id);
			if(companyToDelete.isPresent()) {
				try {
					companyService.deleteService(companyToDelete.get());
		        } catch(DaoException e) {
		        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Deletion : An error has occured during the deletion...", e);
		        }
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Company " + id + " doesn't exist.");
			}
		} catch(DaoException e) {
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Find Company : An error has occured during the research...", e);
        }
    }
}
