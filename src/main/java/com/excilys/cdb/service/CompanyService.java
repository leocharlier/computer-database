package com.excilys.cdb.service;

import java.util.ArrayList;

import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoException;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.model.Company;

public class CompanyService {
	private CompanyDao companyDao;
	
	public CompanyService(DaoFactory daoFactory) {
		this.companyDao = daoFactory.getCompanyDao();
	}
	
	public ArrayList<Company> listService() throws DaoException{
		return companyDao.list();
	}
}
