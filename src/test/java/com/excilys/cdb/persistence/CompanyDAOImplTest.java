package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;

class CompanyDAOImplTest {
	
	private static DAOFactory daoFactory;
	private static CompanyDAO companyDAO;
	private static Logger logger = LoggerFactory.getLogger(CompanyDAOImplTest.class);
	private final int NB_COMPANIES = 42;
	private Company company;
	
	@BeforeAll
	public static void setUp() {
		daoFactory = DAOFactory.getInstance();
		companyDAO = daoFactory.getCompanyDao();
	}

	@Test
	void listCompaniesTest() {
		try {
			ArrayList<Company> companies = companyDAO.list();
			assertEquals(companies.size(), this.NB_COMPANIES);
		} catch( DAOException e ) {
			logger.warn( "List companies test failed." );
			fail( "List companies test failed : database error." );
		}
	}
	
	@Test
	void findCompanyTest() {
		int companyId = 12;
		try {
			company = companyDAO.find( companyId );
			assertEquals( company.getId(), companyId );
		} catch( DAOException e ) {
			logger.warn( "Find company test failed." );
			fail( "Find company test failed : database error." );
		}
		
	}
	
	@Test
	void findUnknownCompanyTest() {
		int unknownId = -1;
		try {
			company = companyDAO.find( unknownId );
			assertNull( company );
		} catch( DAOException e ) {
			logger.warn( "Find unknown company test failed." );
			fail( "Find unknown company test failed : database error." );
		}
	}

}
