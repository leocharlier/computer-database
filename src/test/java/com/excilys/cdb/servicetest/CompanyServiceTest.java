package com.excilys.cdb.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.HibernateConfiguration;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;

class CompanyServiceTest {
  @Autowired
  private static CompanyService companyService;
  static final Logger LOGGER = Logger.getLogger(CompanyServiceTest.class);
  private static final int NB_COMPANIES = 42;
  private Optional<Company> company;
  
  @BeforeAll
  public static void setUp() {
	AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(HibernateConfiguration.class);
//    CliUI cliUI = applicationContext.getBean("cliUI", CliUI.class);
//    daoFactory = DaoFactory.getInstance();
//    companyService = new CompanyService(daoFactory);
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
  void findCompanyIdTest() {
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
  void findUnknownCompanyIdTest() {
    int unknownId = -1;
    try {
        company = companyService.findById(unknownId);
        assertEquals(company, Optional.empty());
      } catch (DaoException e) {
        LOGGER.warn("Find company test failed.");
        fail("Find company test failed : database error.");
      }
  }
  
  @Test
  void findCompanyNameTest() {
    String companyName = "Thinking Machines";
    try {
      company = companyService.findByName(companyName);
      assertEquals(company.get().getName(), companyName);
    } catch (DaoException e) {
      LOGGER.warn("Find company test failed.");
      fail("Find company test failed : database error.");
    }
  }
  
  @Test
  void findUnknownCompanyNameTest() {
    String unknownName = "unkown";
    try {
        company = companyService.findByName(unknownName);
        assertEquals(company, Optional.empty());
      } catch (DaoException e) {
        LOGGER.warn("Find company test failed.");
        fail("Find company test failed : database error.");
      }
  }

}
