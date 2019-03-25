package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class DaoException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DaoException.class);
   
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
