package com.excilys.cdb.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOException extends RuntimeException {
	final static Logger logger = LoggerFactory.getLogger( DAOException.class );
    
	public DAOException( String message ) {
        super( message );
        logger.warn(message);
    }

    public DAOException( String message, Throwable cause ) {
        super( message, cause );
        logger.warn(cause.toString() + " : " + message);
    }

    public DAOException( Throwable cause ) {
        super( cause );
        logger.warn(cause.toString());
    }
    
}
