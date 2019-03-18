package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.model.Company;

class CompanyDAOImplTest {
	
	private CompanyDAO companyDAO;

	@Test
	void listCompanyTest() {
		ArrayList<Company> companies = companyDAO.list();
		System.out.println("yo");
		assertEquals(companies.size(), 43);
	}

}
