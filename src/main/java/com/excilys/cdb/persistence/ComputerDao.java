package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
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
    ArrayList<Computer> computers = (ArrayList<Computer>) jdbcTemplate.query(SQL_SELECT_ALL, computerMapper);
    return computers;
  }

  public Optional<Computer> findById(int pid) {
    Computer company;
	try {
		company = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{pid}, computerMapper);
	} catch(EmptyResultDataAccessException e){
		company = null;
	}
    
    return Optional.ofNullable(company);
  }

  public void create(Computer computer) throws DaoException {
	int affectedLines = jdbcTemplate.update(SQL_INSERT,
			computer.getName(),
			computer.getIntroduced().orElse(null),
			computer.getDiscontinued().orElse(null),
			computer.getCompany().map(someCompany -> someCompany.getId()).orElse(null));
    
	if(affectedLines < 1) {
		throw new DaoException("Failed to create the computer in database, no line added in the table.");
	}
  }

  public void update(Computer computerUpdated) throws DaoException {
    int affectedLines = jdbcTemplate.update(SQL_UPDATE,
    		computerUpdated.getName(),
    		computerUpdated.getIntroduced().orElse(null),
    		computerUpdated.getDiscontinued().orElse(null),
    		computerUpdated.getCompany().map(someCompany -> someCompany.getId()).orElse(null),
    		computerUpdated.getId());
    
	if(affectedLines < 1) {
		throw new DaoException("Failed to update the computer in database, no line updated in the table.");
	}
  }

  public void delete(Computer computer) throws DaoException {
	try {
		jdbcTemplate.update(SQL_DELETE, computer.getId());
	} catch(DataAccessException e) {
		throw new DaoException("Failed to update the computer in database, no line updated in the table.");
	}
  }
  
  public ArrayList<Computer> search(String search) throws DaoException {
    ArrayList<Computer> computers = (ArrayList<Computer>) jdbcTemplate.query(SQL_SEARCH, new Object[] {search, search }, computerMapper);
    return computers;
  }

  public Integer getCompanyId(Computer computer) {
	return computer.getCompany().map(someCompany -> someCompany.getId()).orElse(null);
  }
}
