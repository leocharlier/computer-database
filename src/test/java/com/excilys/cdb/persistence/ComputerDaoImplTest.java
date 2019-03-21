package com.excilys.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.excilys.cdb.model.Computer;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ComputerDaoImplTest {

  private static DaoFactory daoFactory;
  private static ComputerDao computerDAO;
  static final Logger LOGGER = Logger.getLogger(CompanyDaoImplTest.class);
  private static DateFormat dateFormat;
  private Optional<Computer> computerToFind;
  private Computer computer;
  
  @BeforeAll
  public static void setUp() {
    daoFactory = DaoFactory.getInstance();
    computerDAO = daoFactory.getComputerDao();
    dateFormat = new SimpleDateFormat("dd/MM/yyyy");
  }

  @Test
  void findComputerTest() {
    int computerId = 123;
    try {
    	computerToFind = computerDAO.find(computerId);
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
    	computerToFind = computerDAO.find(unknownId);
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
      int nbComputersBeforeCreation = computerDAO.list().size();
      computerDAO.create(computer);
      int nbComputersAfterCreation = computerDAO.list().size();
      assertEquals(nbComputersAfterCreation, nbComputersBeforeCreation + 1);
      computerDAO.delete(computer);
    } catch (DaoException e) {
      LOGGER.warn("Create computer test failed.");
      fail("Create computer test failed : database error.");
    }
  }
  
  @Test
  void createNullComputerTest() {
    assertThrows(NullPointerException.class, () -> {
      computerDAO.create(null);
    });
  }
  
  @Test
  void createComputerWithoutNameTest() {
    computer = new Computer();
    assertThrows(DaoException.class, () -> {
      computerDAO.create(computer);
    });
  }
  
  @Test
  void createComputerWithNameAndDiscontinuedOnly() {  
    Date date = new Date();
    long time = date.getTime();
    Timestamp dateTest = new Timestamp(time);
    computer = new Computer("TestCreation", dateTest);
    assertThrows(DaoException.class, () -> {
      computerDAO.create(computer);
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
      assertThrows(DaoException.class, () -> {
        computerDAO.create(computer);
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
      computerDAO.create(computer);
      String newName = "New Computer Name";
      computer.setName(newName);
      computerDAO.update(computer);
      assertEquals(computerDAO.find(computer.getId()).get().getName(), newName);
      computerDAO.delete(computer);
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
      computerDAO.create(computer);
      
      date = dateFormat.parse("05/06/1980");
      time = date.getTime();
      Timestamp discontinuedDateUpdate = new Timestamp(time);
      computer.setDiscontinued(discontinuedDateUpdate);
      
      assertThrows(DaoException.class, () -> {
        computerDAO.update(computer);
      });
      
      computerDAO.delete(computer);
    } catch (ParseException e) {
      LOGGER.warn("Update computer discontinued date test failed.");
      fail("Update computer discontinued date test failed : parsing error.");
    }
  }
  
  @Test
  void deleteComputerTest() {
    computer = new Computer("TestDelete");
    try {
      computerDAO.create(computer);
      int nbComputersBeforeDeletion = computerDAO.list().size();
      computerDAO.delete(computer);
      int nbComputersAfterDeletion = computerDAO.list().size();
      assertEquals(nbComputersAfterDeletion, nbComputersBeforeDeletion - 1);
    } catch (DaoException e) {
      LOGGER.warn("Delete computer test failed.");
      fail("Delete computer test failed : database error.");
    }
  }
  
  @Test
  void deleteNullComputerTest() {
    assertThrows(NullPointerException.class, () -> {
      computerDAO.delete(null);
    });
  }
  
  @Test
  void deleteUnknownComputerTest() {
    computer = new Computer();
    assertThrows(DaoException.class, () -> {
      computerDAO.delete(computer);
    });
  }

}
