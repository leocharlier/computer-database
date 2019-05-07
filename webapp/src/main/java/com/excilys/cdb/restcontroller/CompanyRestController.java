package com.excilys.cdb.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Company's list is empty.");
		}
	}
}
