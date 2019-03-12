package com.excilys.persistence;

import java.sql.*;

public final class DAOUtility {

	public static PreparedStatement preparedStatementInitialization( Connection connection, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException {
	    PreparedStatement preparedStatement = connection.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
	    for ( int i = 0; i < objets.length; i++ ) {
	        preparedStatement.setObject( i + 1, objets[i] );
	    }
	    return preparedStatement;
	}
	
	public static void closure( ResultSet resultSet ) {
	    if ( resultSet != null ) {
	        try {
	            resultSet.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Failed to close the ResultSet : " + e.getMessage() );
	        }
	    }
	}

	public static void closure( Statement statement ) {
	    if ( statement != null ) {
	        try {
	            statement.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Failed to close the Statement : " + e.getMessage() );
	        }
	    }
	}

	public static void closure( Connection connection ) {
	    if ( connection != null ) {
	        try {
	        	connection.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Failed to close the connection : " + e.getMessage() );
	        }
	    }
	}

	public static void closures( Statement statement, Connection connection ) {
		closure( statement );
		closure( connection );
	}

	public static void closures( ResultSet resultSet, Statement statement, Connection connection ) {
		closure( resultSet );
		closure( statement );
		closure( connection );
	}
}
