package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class DiscontinuedBeforeIntroducedException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DiscontinuedBeforeIntroducedException.class);
  
  public DiscontinuedBeforeIntroducedException(String message) {
    super(message);
    LOGGER.error(message);
  }

  public DiscontinuedBeforeIntroducedException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.error(cause.toString() + " : " + message);
  }

  public DiscontinuedBeforeIntroducedException(Throwable cause) {
    super(cause);
    LOGGER.error(cause.toString());
  }
}