package com.excilys.cdb.persistence;

import java.util.ArrayList;

import com.excilys.cdb.model.Company;

public interface CompanyDAO {

    ArrayList<Company> list() throws DAOException;
    
    Company find( int pId ) throws DAOException;

}