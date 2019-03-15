package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

public class CompanyMapper {
	
	public Company map( ResultSet resultSet ) throws SQLException {
	    Company company = new Company();
	    company.setId( resultSet.getInt( "id" ) );
	    company.setName( resultSet.getString( "name" ) );
	    return company;
	}

}