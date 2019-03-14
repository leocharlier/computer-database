package com.excilys.persistence;

import static com.excilys.persistence.DAOUtility.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;

public class CompanyDAOImpl implements CompanyDAO {
	private DAOFactory daoFactory;
	private CompanyMapper companyMapper;
	
	private static final String SQL_SELECT_ALL = "SELECT id, name FROM company;";
	private static final String SQL_SELECT_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	
	CompanyDAOImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
		this.companyMapper = new CompanyMapper();
	}

	@Override
	public ArrayList<Company> list() throws DAOException {
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    ArrayList<Company> companies = new ArrayList<Company>();
	    
	    try {
	    	connection = daoFactory.getConnection();
	        preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_ALL, false );
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	        	companies.add( this.companyMapper.map( resultSet ) );
	        }
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			closures( resultSet, preparedStatement, connection );
	    }
	    
		return companies;
	}

	@Override
	public Company find( int pId ) throws DAOException {
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Company company = null;
	    
	    try {
	    	connection = daoFactory.getConnection();
	        preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_BY_ID, false, pId );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	        	company = this.companyMapper.map( resultSet );
	        } else {
	        	throw new DAOException("No SQL result for this company ID.");
	        }
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			closures( resultSet, preparedStatement, connection );
	    }
	    
	    return company;
	}

}
