package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;

public class CompanyService {
	private CompanyDao companyDao;
	
	public CompanyService(DaoFactory daoFactory) {
		this.companyDao = daoFactory.getCompanyDao();
	}
	
	public ArrayList<Company> listService() throws DaoException {
		return companyDao.list();
	}
	
	public Optional<Company> findById(int id) throws DaoException {
		return companyDao.findById(id);
	}
	
	public Optional<Company> findByName(String name) throws DaoException {
		return companyDao.findByName(name);
	}
} 
