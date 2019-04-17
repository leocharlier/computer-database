package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.ComputerDaoMapper;
import com.excilys.cdb.model.Computer;

@Lazy
@Repository
public class ComputerDao {
  static final Logger LOGGER = Logger.getLogger(ComputerDao.class);
  private ComputerDaoMapper computerMapper;
  private JdbcTemplate jdbcTemplate;
  
  public ComputerDao(ComputerDaoMapper cm, JdbcTemplate jdbcT) {
	  jdbcTemplate = jdbcT;
	  computerMapper = cm;
  }

  private static final String SQL_SELECT_BY_ID = 
      "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?";
  private static final String SQL_SELECT_ALL = 
      "SELECT id, name, introduced, discontinued, company_id FROM computer";
  private static final String SQL_INSERT = 
      "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
  private static final String SQL_DELETE = 
      "DELETE FROM computer WHERE id = ?";
  private static final String SQL_UPDATE = 
      "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
  private static final String SQL_SEARCH = 
	  "SELECT cpt.id, cpt.name, cpt.introduced, cpt.discontinued, cpt.company_id FROM computer cpt LEFT OUTER JOIN company cpn ON cpt.company_id=cpn.id WHERE cpt.name LIKE ? OR cpn.name LIKE ?";

  public ArrayList<Computer> list() {
	try {
		return (ArrayList<Computer>) jdbcTemplate.query(SQL_SELECT_ALL, computerMapper);
	} catch(DataAccessException e) {
		throw new DaoException("Failed to list the computers.", e.getCause());
	}
  }

  public Optional<Computer> findById(int pid) {
    Computer computer;
	try {
		computer = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{pid}, computerMapper);
		LOGGER.info("Computer " + computer.getId() + " found.");
	} catch(EmptyResultDataAccessException e){
		computer = null;
		LOGGER.warn("The computer " + pid + " doesn't exist.");
	} catch(DataAccessException e) {
		throw new DaoException("Failed to find the computer " + pid + ".", e.getCause());
	}
    
    return Optional.ofNullable(computer);
  }

  public void create(Computer computer) throws DaoException {
	try {
		int affectedLines = jdbcTemplate.update(SQL_INSERT,
				computer.getName(),
				computer.getIntroduced().orElse(null),
				computer.getDiscontinued().orElse(null),
				computer.getCompany().map(someCompany -> someCompany.getId()).orElse(null));
	    
		if(affectedLines < 1) {
			throw new DaoException("Failed to create the computer in database, no line added in the table.");
		}
		
		LOGGER.info("Computer '" + computer.getName() + "' created.");
	} catch(DataAccessException e) {
		throw new DaoException("Failed to create the computer '" + computer.getName() + "'.", e.getCause());
	}
  }

  public void update(Computer computerUpdated) throws DaoException {
	try {
		int affectedLines = jdbcTemplate.update(SQL_UPDATE,
	    		computerUpdated.getName(),
	    		computerUpdated.getIntroduced().orElse(null),
	    		computerUpdated.getDiscontinued().orElse(null),
	    		computerUpdated.getCompany().map(someCompany -> someCompany.getId()).orElse(null),
	    		computerUpdated.getId());
	    
		if(affectedLines < 1) {
			throw new DaoException("Failed to update the computer in database, no line updated in the table.");
		}
		
		LOGGER.info("Computer " + computerUpdated.getId() + " updated.");
	} catch(DataAccessException e) {
		throw new DaoException("Failed to update the computer " + computerUpdated.getId() + ".", e.getCause());
	}
    
  }

  public void delete(Computer computer) throws DaoException {
	try {
		jdbcTemplate.update(SQL_DELETE, computer.getId());
		LOGGER.info("Computer " + computer.getId() + " deleted.");
	} catch(DataAccessException e) {
		throw new DaoException("Failed to delete the computer " + computer.getId() + ".", e.getCause());
	}
  }
  
  public ArrayList<Computer> search(String search) throws DaoException {
	try {
		ArrayList<Computer> computers =  (ArrayList<Computer>) jdbcTemplate.query(SQL_SEARCH, new Object[] {"%"+search+"%", "%"+search+"%"}, computerMapper);
		LOGGER.info(computers.size() + " computers found for search '" + search + "'.");
		return computers;
	} catch(DataAccessException e) {
		throw new DaoException("Failed to create computers list for search '" + search + "'.", e.getCause());
	}
  }

  public Integer getCompanyId(Computer computer) {
	return computer.getCompany().map(someCompany -> someCompany.getId()).orElse(null);
  }
}
