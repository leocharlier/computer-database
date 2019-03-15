package com.excilys.cdb.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOConfigurationException extends RuntimeException{
	final static Logger logger = LoggerFactory.getLogger( "com.excilys.cdb.persistence.DAOConfigurationException" );

	public DAOConfigurationException( String message ) {
        super( message );
        logger.error(message);
    }

    public DAOConfigurationException( String message, Throwable cause ) {
        super( message, cause );
        logger.error(cause.toString() + " : " + message);
    }

    public DAOConfigurationException( Throwable cause ) {
        super( cause );
        logger.error(cause.toString());
    }
}
