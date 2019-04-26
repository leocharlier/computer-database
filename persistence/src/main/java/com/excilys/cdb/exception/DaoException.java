package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DaoException.class);
   
  public DaoException(String message) {
    super(message);
    LOGGER.error(message);
  }

  public DaoException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.error(cause.toString() + " : " + message);
  }

  public DaoException(Throwable cause) {
    super(cause);
    LOGGER.error(cause.toString());
  }
    
}
