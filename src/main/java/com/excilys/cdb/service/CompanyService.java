package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;

@Lazy
@Service
public class CompanyService {
	@Autowired
	private CompanyDao companyDao;
	
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
