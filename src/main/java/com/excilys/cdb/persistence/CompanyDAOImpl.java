package com.excilys.cdb.persistence;

import static com.excilys.cdb.persistence.DAOUtility.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

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
		ArrayList<Company> companies = new ArrayList<Company>();
	    ResultSet resultSet = null;
	    
	    try (
	    	Connection connection = daoFactory.getConnection();
	    	PreparedStatement preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_ALL, false )
	    	) {
	    	
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	        	companies.add( this.companyMapper.map( resultSet ) );
	        }
	        
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		}
	    
		return companies;
	}

	@Override
	public Company find( int pId ) throws DAOException {
	    ResultSet resultSet = null;
	    Company company = null;
	    
	    try (
	    	Connection connection = daoFactory.getConnection();
	    	PreparedStatement preparedStatement = preparedStatementInitialization( connection, SQL_SELECT_BY_ID, false, pId )
	    	) {
	    	
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	        	company = this.companyMapper.map( resultSet );
	        } else {
	        	throw new DAOException("No SQL result for this company ID.");
	        }
	        
	    } catch ( SQLException e ) {
			throw new DAOException( e );
		}
	    
	    return company;
	}

}
