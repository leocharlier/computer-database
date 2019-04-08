package com.excilys.cdb.persistence;

import static com.excilys.cdb.persistence.DaoUtility.preparedStatementInitialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyDaoMapper;
import com.excilys.cdb.model.Company;

public class CompanyDao {
  static final Logger LOGGER = Logger.getLogger(CompanyDao.class);
  private final DaoFactory daoFactory;
  private CompanyDaoMapper companyMapper;
  private static final String SQL_SELECT_ALL      = "SELECT id, name FROM company;";
  private static final String SQL_SELECT_BY_ID    = "SELECT id, name FROM company WHERE id = ?;";
  private static final String SQL_SELECT_BY_NAME  = "SELECT id, name FROM company WHERE name = ?;";
  private static final String SQL_DELETE          = "DELETE FROM company WHERE id = ?";
  private static final String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE company_id = ?";

  CompanyDao(final DaoFactory daoFactory) {
    this.daoFactory = daoFactory;
    this.companyMapper = new CompanyDaoMapper();
  }
  
  public ArrayList<Company> list() throws DaoException {
    LOGGER.info("Start companies listing...");
    ArrayList<Company> companies = new ArrayList<Company>();
    ResultSet resultSet = null;
    
    try (
        Connection connection = daoFactory.getConnection();
        PreparedStatement preparedStatement = 
            preparedStatementInitialization(connection, SQL_SELECT_ALL, false)
        ) {

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        companies.add(this.companyMapper.map(resultSet));
      }
      
      connection.commit();
    } catch (SQLException e) {
      throw new DaoException(e);
    }

    if (companies.isEmpty()) {
      LOGGER.warn("Companies list is empty.");
    } else {
      LOGGER.info("Comapnies list created.");
    }

    return companies;
  }

  public Optional<Company> findById(int pid) throws DaoException {
    ResultSet resultSet;
    Company company = null;

    try (
        Connection connection = daoFactory.getConnection();
        PreparedStatement preparedStatement =
            preparedStatementInitialization(connection, SQL_SELECT_BY_ID, false, pid)
    ) {
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        company = this.companyMapper.map(resultSet);
      }
      
      connection.commit();
    } catch (SQLException e) {
      throw new DaoException(e);
    }

    return Optional.ofNullable(company);
  }
  
  public Optional<Company> findByName(String pname) throws DaoException {
    ResultSet resultSet;
    Company company = null;

    try (
        Connection connection = daoFactory.getConnection();
        PreparedStatement preparedStatement =
            preparedStatementInitialization(connection, SQL_SELECT_BY_NAME, false, pname)
    ) {
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        company = this.companyMapper.map(resultSet);
      }
      
      connection.commit();
    } catch (SQLException e) {
      throw new DaoException(e);
    }

    return Optional.ofNullable(company);
  }
  
  public void delete(Company company) throws DaoException {
	    try (
	         Connection connection = daoFactory.getConnection();
	         PreparedStatement preparedStatement = preparedStatementInitialization(connection, SQL_DELETE, false, company.getId());
	    	 PreparedStatement preparedStatementComputer = preparedStatementInitialization(connection, SQL_DELETE_COMPUTER, false, company.getId());
	    ) {
    	  int statut = preparedStatementComputer.executeUpdate();
	      if (statut == 0) {
	        throw new DaoException("Failed to delete the computer(s) related to the company in database, no line deleted in the computer table.");
	      } else {
	    	  LOGGER.info(statut + " computers deleted.");
	      }
	      
	      statut = preparedStatement.executeUpdate();
	      if (statut == 0) {
	        throw new DaoException("Failed to delete the company in database, no line deleted in the company table.");
	      }
	      
	      connection.commit();
	    } catch (SQLException e) {
	      throw new DaoException("SQL error during deletion.", e);
	    }

	    LOGGER.info("Company " + company.getId() + " deleted.");
	  }

}
