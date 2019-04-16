package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyDaoMapper;
import com.excilys.cdb.model.Company;

@Lazy
@Repository
public class CompanyDao {
  static final Logger LOGGER = Logger.getLogger(CompanyDao.class);
  private CompanyDaoMapper companyMapper;
  private JdbcTemplate jdbcTemplate;
  
  public CompanyDao(CompanyDaoMapper cm, JdbcTemplate jdbcT) {
	  jdbcTemplate = jdbcT;
	  companyMapper = cm;
  }
  
  private static final String SQL_SELECT_ALL      = "SELECT id, name FROM company;";
  private static final String SQL_SELECT_BY_ID    = "SELECT id, name FROM company WHERE id = ?;";
  private static final String SQL_SELECT_BY_NAME  = "SELECT id, name FROM company WHERE name = ?;";
  private static final String SQL_DELETE          = "DELETE FROM company WHERE id = ?";
  private static final String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE company_id = ?";
  
  public ArrayList<Company> list() {
    ArrayList<Company> companies = (ArrayList<Company>) jdbcTemplate.query(SQL_SELECT_ALL, companyMapper);
    return companies;
  }

  public Optional<Company> findById(int pid) {
	Company company;
	try {
		company = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{pid}, companyMapper);
	} catch(EmptyResultDataAccessException e){
		company = null;
	}
    
    return Optional.ofNullable(company);
  }
  
  public Optional<Company> findByName(String pname) {
    Company company;
	try {
		company = jdbcTemplate.queryForObject(SQL_SELECT_BY_NAME, new Object[]{pname}, companyMapper);
	} catch(EmptyResultDataAccessException e){
		company = null;
	}
    
    return Optional.ofNullable(company);
  }
  
  @Transactional(rollbackFor=DaoException.class)
  public void delete(Company company) throws DaoException {
	try {
	  int computersDeleted = jdbcTemplate.update(SQL_DELETE_COMPUTER, company.getId());
	  LOGGER.info(computersDeleted + " computers deleted.");
	  int companyDeleted = jdbcTemplate.update(SQL_DELETE, company.getId());
	  if(companyDeleted < 1) {
		  throw new DaoException("Failed to delete the company " + company.getId() + ". Cause : None line affected in database.");
	  }
	  LOGGER.info("Company " + company.getId() + " deleted.");
	} catch(DataAccessException e) {
		LOGGER.error("Failed to delete the company " + company.getId() + ".");
		throw new DaoException("Failed to delete the company " + company.getId() + ". Cause : " + e.getMessage());
	}
  }
}
