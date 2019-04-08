package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;

class CompanyDaoImplTest {

  private static DaoFactory daoFactory;
  private static CompanyService companyService;
  static final Logger LOGGER = Logger.getLogger(CompanyDaoImplTest.class);
  private static final int NB_COMPANIES = 42;
  private Optional<Company> company;
  
  @BeforeAll
  public static void setUp() {
    daoFactory = DaoFactory.getInstance();
    companyService = new CompanyService(daoFactory);
  }

  @Test
  void listCompaniesTest() {
    try {
      ArrayList<Company> companies = companyService.listService();
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
      company = companyService.findById(companyId);
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
        company = companyService.findById(unknownId);
        assertEquals(company, Optional.empty());
      } catch (DaoException e) {
        LOGGER.warn("Find company test failed.");
        fail("Find company test failed : database error.");
      }
  }

}
