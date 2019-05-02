package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class EmptyUsernameException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(EmptyUsernameException.class);
  
  public EmptyUsernameException(String message) {
    super(message);
    LOGGER.error(message);
  }
}