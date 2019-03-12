package com.excilys.persistence;

import java.sql.*;
import java.util.ArrayList;

import static com.excilys.persistence.DAOUtility.*;

import com.excilys.model.Computer;

public class ComputerDAOImpl implements ComputerDAO {
	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT_BY_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private static final String SQL_SELECT_ALL = "SELECT id, name, introduced, discontinued, company_id FROM computer;";
	private static final String SQL_INSERT = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
	
	ComputerDAOImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}
	
	private static Computer map( ResultSet resultSet ) throws SQLException {
	    Computer computer = new Computer();
	    DAOFactory daoFactory = DAOFactory.getInstance();
	    CompanyDAO companyDAO = daoFactory.getCompanyDao();
	    
	    computer.setId( resultSet.getInt( "id" ) );
	    computer.setName( resultSet.getString( "name" ) );
	    computer.setIntroduced( resultSet.getTimestamp( "introduced" ) );
	    computer.setDiscontinued( resultSet.getTimestamp( "discontinued" ) );
	    computer.setCompany( companyDAO.find( resultSet.getInt( "company_id" ) ) );
	    
	    return computer;
	}

	@Override
	public ArrayList<Computer> list() throws DAOException {
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    ArrayList<Computer> computers = new ArrayList<Computer>();
	    
	    try {
	    	connection = daoFactory.getConnection();
	        preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_ALL, false );
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	        	computers.add( map( resultSet ) );
	        }
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			closures( resultSet, preparedStatement, connection );
	    }
	    
		return computers;
	}

	@Override
	public Computer find(int pId) throws DAOException {
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Computer computer = null;
	    
	    try {
	    	connection = daoFactory.getConnection();
	        preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_BY_ID, false, pId );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	        	computer = map( resultSet );
	        }
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			closures( resultSet, preparedStatement, connection );
	    }
	    
		return computer;
	} 

	@Override
	public void create(Computer computer) throws DAOException {

	}

	@Override
	public void update(Computer computer) throws DAOException {

	}

	@Override
	public void delete(Computer computer) throws DAOException {

	}

}
