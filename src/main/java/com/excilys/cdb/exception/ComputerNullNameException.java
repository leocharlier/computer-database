package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class ComputerNullNameException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(ComputerNullNameException.class);
   
  public ComputerNullNameException(String message) {
    super(message);
    LOGGER.warn(message);
  }
}