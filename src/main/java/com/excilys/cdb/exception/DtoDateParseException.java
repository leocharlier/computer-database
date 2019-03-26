package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class DtoDateParseException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DtoDateParseException.class);
   
  public DtoDateParseException(String message) {
    super(message);
    LOGGER.warn(message);
  }

  public DtoDateParseException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.warn(cause.toString() + " : " + message);
  }
}
