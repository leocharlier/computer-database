package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class DiscontinuedBeforeIntroducedException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DiscontinuedBeforeIntroducedException.class);
  
  public DiscontinuedBeforeIntroducedException(String message) {
    super(message);
    LOGGER.error(message);
  }
}