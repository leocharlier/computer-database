package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class DaoConfigurationException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DaoConfigurationException.class);

  public DaoConfigurationException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.error(cause.toString() + " : " + message);
  }
}
