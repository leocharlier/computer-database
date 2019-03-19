package com.excilys.cdb.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoException extends RuntimeException {
  static final Logger LOGGER = LoggerFactory.getLogger(DaoException.class);
   
  public DaoException(String message) {
    super(message);
    LOGGER.warn(message);
  }

  public DaoException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.warn(cause.toString() + " : " + message);
  }

  public DaoException(Throwable cause) {
    super(cause);
    LOGGER.warn(cause.toString());
  }
    
}
