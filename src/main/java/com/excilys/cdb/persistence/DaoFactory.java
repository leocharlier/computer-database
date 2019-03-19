package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoFactory {

  static final Logger LOGGER = LoggerFactory.getLogger(DaoFactory.class);

  private static final String FICHIER_PROPERTIES = "properties/dao.properties";
  private static final String PROPERTY_URL = "url";
  private static final String PROPERTY_DRIVER = "driver";
  private static final String PROPERTY_USER = "user";
  private static final String PROPERTY_PWD = "pwd";

  private String url;
  private String username;
  private String password;

  DaoFactory(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public static DaoFactory getInstance() throws DaoConfigurationException {
    Properties properties = new Properties();
    String url;
    String driver;
    String user;
    String pwd;

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);

    if (fichierProperties == null) {
      throw new DaoConfigurationException("The file " + FICHIER_PROPERTIES + " is unreachable.");
    }

    try {
      properties.load(fichierProperties);
      url = properties.getProperty(PROPERTY_URL);
      driver = properties.getProperty(PROPERTY_DRIVER);
      user = properties.getProperty(PROPERTY_USER);
      pwd = properties.getProperty(PROPERTY_PWD);
    } catch (IOException e) {
      throw new DaoConfigurationException("Impossible to load the property file "
         + FICHIER_PROPERTIES, e);
    }

    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      throw new DaoConfigurationException("The driver is unreachable in the classpath.", e);
    }

    DaoFactory instance = new DaoFactory(url, user, pwd);
    return instance;
  }

  protected Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }

  public CompanyDao getCompanyDao() {
    return new CompanyDaoImpl(this);
  }
    
  public ComputerDao getComputerDao() {
    return new ComputerDaoImpl(this);
  }
}
