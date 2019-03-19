package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.excilys.cdb.model.Company;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CompanyDaoImplTest {

  private static DaoFactory daoFactory;
  private static CompanyDao companyDAO;
  private static Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImplTest.class);
  private static final int NB_COMPANIES = 42;
  private Company company;
  
  @BeforeAll
  public static void setUp() {
    daoFactory = DaoFactory.getInstance();
    companyDAO = daoFactory.getCompanyDao();
  }

  @Test
  void listCompaniesTest() {
    try {
      ArrayList<Company> companies = companyDAO.list();
      assertEquals(companies.size(), NB_COMPANIES);
    } catch (DaoException e) {
      LOGGER.warn("List companies test failed.");
      fail("List companies test failed : database error.");
    }
  }
  
  @Test
  void findCompanyTest() {
    int companyId = 12;
    try {
      company = companyDAO.find(companyId);
      assertEquals(company.getId(), companyId);
    } catch (DaoException e) {
      LOGGER.warn("Find company test failed.");
      fail("Find company test failed : database error.");
    }
    
  }
  
  @Test
  void findUnknownCompanyTest() {
    int unknownId = -1;
    try {
      company = companyDAO.find(unknownId);
      assertNull(company);
    } catch (DaoException e) {
      LOGGER.warn("Find unknown company test failed.");
      fail("Find unknown company test failed : database error.");
    }
  }

}
