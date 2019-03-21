package com.excilys.cdb.service;

import java.util.ArrayList;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoException;
import com.excilys.cdb.persistence.DaoFactory;

public class ComputerService {
	private ComputerDao computerDao;
	
	public ComputerService(DaoFactory daoFactory) {
		this.computerDao = daoFactory.getComputerDao();
	}
	
	public ArrayList<Computer> listService() throws DaoException {
		return this.computerDao.list();
	}

}
