package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.cdb.exception.DaoConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DaoFactory {
	private static final String FICHIER_PROPERTIES = "/properties/hikari.properties";
  private HikariDataSource dataSource;
  private static DaoFactory INSTANCE = null;
  
  private DaoFactory(HikariDataSource ds){
	  this.dataSource = ds;
  }

  public static DaoFactory getInstance() throws DaoConfigurationException {
	  if(INSTANCE == null) {
		  try {
			  HikariConfig config = new HikariConfig(FICHIER_PROPERTIES);
			  HikariDataSource dataSource = new HikariDataSource(config);
			  INSTANCE = new DaoFactory(dataSource);
		  } catch(IllegalArgumentException e) {
			  throw new DaoConfigurationException("Impossible to load the property file " + FICHIER_PROPERTIES, e);
		  } catch(RuntimeException e) {
			  throw new DaoConfigurationException("Property file issue in " + FICHIER_PROPERTIES, e);
		  }
	  }
	  
	  return INSTANCE;
  }

  protected Connection getConnection() throws SQLException {
	  return dataSource.getConnection();
  }

  public CompanyDao getCompanyDao() {
    return new CompanyDao(this);
  }
    
  public ComputerDao getComputerDao() {
    return new ComputerDao(this);
  }
}
