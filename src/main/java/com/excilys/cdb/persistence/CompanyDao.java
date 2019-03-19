package com.excilys.cdb.persistence;

import com.excilys.cdb.model.Company;

import java.util.ArrayList;

public interface CompanyDao {

  ArrayList<Company> list() throws DaoException;
    
  Company find(int pid) throws DaoException;

}