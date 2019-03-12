package com.excilys.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {

    private static final String FICHIER_PROPERTIES = "com/excilys/persistence/dao.properties";
    private static final String PROPERTY_URL	   = "url";
    private static final String PROPERTY_DRIVER	   = "driver";
    private static final String PROPERTY_USER	   = "user";
    private static final String PROPERTY_PWD	   = "pwd";

    private String              url;
    private String              username;
    private String              password;

    DAOFactory( String url, String username, String password ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DAOFactory getInstance() throws DAOConfigurationException {
        Properties properties = new Properties();
        String url;
        String driver;
        String user;
        String pwd;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "The file " + FICHIER_PROPERTIES + " is unreachable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            user = properties.getProperty( PROPERTY_USER );
            pwd = properties.getProperty( PROPERTY_PWD );
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Impossible to load the property file " + FICHIER_PROPERTIES, e );
        }

        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "The driver is unreachable in the classpath.", e );
        }

        DAOFactory instance = new DAOFactory( url, user, pwd );
        return instance;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection( url, username, password );
    }

    public CompanyDAO getCompanyDao() {
        return new CompanyDAOImpl( this );
    }
    
    public ComputerDAO getComputerDao() {
        return new ComputerDAOImpl( this );
    }
}
