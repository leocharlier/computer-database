package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.excilys.cdb.model.Company;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CompanyDaoImplTest {

  private static DaoFactory daoFactory;
  private static CompanyDao companyDAO;
  static final Logger LOGGER = Logger.getLogger(CompanyDaoImplTest.class);
  private static final int NB_COMPANIES = 42;
  private Optional<Company> company;
  
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
      assertEquals(company.get().getId(), companyId);
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
        assertEquals(company, Optional.empty());
      } catch (DaoException e) {
        LOGGER.warn("Find company test failed.");
        fail("Find company test failed : database error.");
      }
  }

}
