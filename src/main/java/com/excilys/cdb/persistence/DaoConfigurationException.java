package com.excilys.cdb.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoConfigurationException extends RuntimeException {
  static final Logger LOGGER = LoggerFactory.getLogger(DaoConfigurationException.class);

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
