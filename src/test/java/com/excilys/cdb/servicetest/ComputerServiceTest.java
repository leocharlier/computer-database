package com.excilys.cdb.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.SpringConfiguration;
import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ComputerService;

class ComputerServiceTest {
  @Autowired
  private static ComputerService computerService;
  static final Logger LOGGER = Logger.getLogger(CompanyServiceTest.class);
  private static DateFormat dateFormat;
  private Optional<Computer> computerToFind;
  private Computer computer;
  
  @BeforeAll
  public static void setUp() {
	AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
  }

  @Test
  void findComputerTest() {
    int computerId = 123;
    try {
    	computerToFind = computerService.findService(computerId);
      assertEquals(computerToFind.get().getId(), computerId);
    } catch (DaoException e) {
      LOGGER.warn("Find computer test failed.");
      fail("Find computer test failed : database error.");
    }
  }
  
  @Test
  void findUnknownComputerTest() {
    int unknownId = -1;
    try {
      computerToFind = computerService.findService(unknownId);
      assertEquals(computerToFind, Optional.empty());
    } catch (DaoException e) {
      LOGGER.warn("Find computer test failed.");
      fail("Find computer test failed : database error.");
    }
  }
  
  @Test
  void createComputerTest() {
    computer = new Computer("TestDelete");
    try {
      int nbComputersBeforeCreation = computerService.listService().size();
      computerService.createService(computer);
      int nbComputersAfterCreation = computerService.listService().size();
      assertEquals(nbComputersAfterCreation, nbComputersBeforeCreation + 1);
      computerService.deleteService(computer);
    } catch (DaoException e) {
      LOGGER.warn("Create computer test failed.");
      fail("Create computer test failed : database error.");
    }
  }
  
  @Test
  void createNullComputerTest() {
    assertThrows(NullPointerException.class, () -> {
    	computerService.createService(null);
    });
  }
  
  @Test
  void createComputerWithoutNameTest() {
    computer = new Computer();
    assertThrows(ComputerNullNameException.class, () -> {
    	computerService.createService(computer);
    });
  }
  
  @Test
  void createComputerWithNameAndDiscontinuedOnly() {  
    Date date = new Date();
    long time = date.getTime();
    Timestamp dateTest = new Timestamp(time);
    computer = new Computer("TestCreation", dateTest);
    assertThrows(DiscontinuedButNoIntroducedException.class, () -> {
    	computerService.createService(computer);
    });
  }
  
  @Test
  void createComputerWithDiscontinuedBeforeIntroduced() throws ParseException {
    try {
      Date date = dateFormat.parse("05/06/1990");
      long time = date.getTime();
      Timestamp introducedDate = new Timestamp(time);
      date = dateFormat.parse("05/06/1980");
      time = date.getTime();
      Timestamp discontinuedDate = new Timestamp(time);
      
      computer = new Computer("TestCreation", introducedDate, discontinuedDate);
      assertThrows(DiscontinuedBeforeIntroducedException.class, () -> {
    	  computerService.createService(computer);
      });
    } catch (ParseException e) {
      LOGGER.warn("Create computer test failed.");
      fail("Create computer test failed : parsing error.");
    }  
  }
  
  @Test
  void updateComputerNameTest() {
    try {
      computer = new Computer("TestUpdate");
      computerService.createService(computer);
      String newName = "New Computer Name";
      computer.setName(newName);
      computerService.updateService(computer);
      assertEquals(computerService.findService(computer.getId()).get().getName(), newName);
      computerService.deleteService(computer);
    } catch (DaoException e) {
      LOGGER.warn("Update computer name test failed.");
      fail("Update computer name test failed : database error.");
    }
  }
  
  @Test
  void updateIllegalDiscontinuedDateTest() {
    try {
      Date date = dateFormat.parse("05/06/1990");
      long time = date.getTime();
      Timestamp introducedDate = new Timestamp(time);
      date = dateFormat.parse("05/06/2000");
      time = date.getTime();
      Timestamp discontinuedDate = new Timestamp(time);
      
      computer = new Computer("TestUpdate", introducedDate, discontinuedDate);
      computerService.createService(computer);
      
      date = dateFormat.parse("05/06/1980");
      time = date.getTime();
      Timestamp discontinuedDateUpdate = new Timestamp(time);
      computer.setDiscontinued(discontinuedDateUpdate);
      
      assertThrows(DiscontinuedBeforeIntroducedException.class, () -> {
    	  computerService.updateService(computer);
      });
      
      computerService.deleteService(computer);
    } catch (ParseException e) {
      LOGGER.warn("Update computer discontinued date test failed.");
      fail("Update computer discontinued date test failed : parsing error.");
    }
  }
  
  @Test
  void deleteComputerTest() {
    computer = new Computer("TestDelete");
    try {
      computerService.createService(computer);
      int nbComputersBeforeDeletion = computerService.listService().size();
      computerService.deleteService(computer);
      int nbComputersAfterDeletion = computerService.listService().size();
      assertEquals(nbComputersAfterDeletion, nbComputersBeforeDeletion - 1);
    } catch (DaoException e) {
      LOGGER.warn("Delete computer test failed.");
      fail("Delete computer test failed : database error.");
    }
  }
  
  @Test
  void deleteNullComputerTest() {
    assertThrows(NullPointerException.class, () -> {
      computerService.deleteService(null);
    });
  }
  
  @Test
  void deleteUnknownComputerTest() {
    computer = new Computer();
    assertThrows(DaoException.class, () -> {
      computerService.deleteService(computer);
    });
  }
  
  @Test
  void searchComputerTest() {
	  ArrayList<Computer> computers = computerService.searchService("apple");
	  assertEquals(computers.size(), 46);
  }
  
  @Test
  void sortByNameAsc() {
	  ArrayList<Computer> computers = computerService.listService();
	  computerService.sortByNameAscService(computers);
	  assertEquals(computers.get(0).getName(), "ACE");
  }
  
  @Test
  void sortByCompanyNameAsc() {
	  ArrayList<Computer> computers = computerService.listService();
	  computerService.sortByCompanyNameAscService(computers);
	  assertEquals(computers.get(0).getCompany().get().getName(), "ACVS");
  }

}
