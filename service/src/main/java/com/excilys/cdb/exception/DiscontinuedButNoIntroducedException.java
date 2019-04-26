package com.excilys.cdb.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class DiscontinuedButNoIntroducedException extends RuntimeException {
  static final Logger LOGGER = Logger.getLogger(DiscontinuedButNoIntroducedException.class);
  
  public DiscontinuedButNoIntroducedException(String message) {
    super(message);
    LOGGER.error(message);
  }
}
