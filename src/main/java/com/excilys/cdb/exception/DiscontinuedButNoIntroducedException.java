package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

public class DiscontinuedButNoIntroducedException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DiscontinuedButNoIntroducedException.class);
  
  public DiscontinuedButNoIntroducedException(String message) {
    super(message);
    LOGGER.error(message);
  }

  public DiscontinuedButNoIntroducedException(String message, Throwable cause) {
    super(message, cause);
    LOGGER.error(cause.toString() + " : " + message);
  }

  public DiscontinuedButNoIntroducedException(Throwable cause) {
    super(cause);
    LOGGER.error(cause.toString());
  }
}
