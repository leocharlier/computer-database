package com.excilys.cdb.persistence;

import org.apache.log4j.Logger;

public class DaoConfigurationException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DaoConfigurationException.class);
  
  public DaoConfigurationException(String message) {
    super(message);
    LOGGER.error(message);
  }

  public DaoConfigurationException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.error(cause.toString() + " : " + message);
  }

  public DaoConfigurationException(Throwable cause) {
    super(cause);
    LOGGER.error(cause.toString());
  }
}
