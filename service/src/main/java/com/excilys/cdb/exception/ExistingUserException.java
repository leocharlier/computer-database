package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ExistingUserException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(ExistingUserException.class);
  
  public ExistingUserException(String message) {
    super(message);
    LOGGER.error(message);
  }
}