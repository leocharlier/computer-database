package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class EmptyPasswordException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(EmptyPasswordException.class);
  
  public EmptyPasswordException(String message) {
    super(message);
    LOGGER.error(message);
  }
}