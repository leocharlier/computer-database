package com.excilys.persistence;

import java.util.ArrayList;

import com.excilys.model.Company;

public interface CompanyDAO {

    ArrayList<Company> list() throws DAOException;
    
    Company find( int pId ) throws DAOException;

}