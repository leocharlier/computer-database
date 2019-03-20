package com.excilys.cdb.persistence;

import static com.excilys.cdb.persistence.DaoUtility.preparedStatementInitialization;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CompanyDaoImpl implements CompanyDao {
  static final Logger LOGGER = Logger.getLogger(CompanyDaoImpl.class);
  private final DaoFactory daoFactory;
  private CompanyMapper companyMapper;
  private static final String SQL_SELECT_ALL = "SELECT id, name FROM company;";
  private static final String SQL_SELECT_BY_ID =
      "SELECT id, name FROM company WHERE id = ?;";

  CompanyDaoImpl(final DaoFactory daoFactory) {
    this.daoFactory = daoFactory;
    this.companyMapper = new CompanyMapper();
  }
  
  @Override
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

  @Override
  public Company find(int pid) throws DaoException {
    ResultSet resultSet = null;
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
    } catch (SQLException e) {
      throw new DaoException(e);
    }

    return company;
  }

}
