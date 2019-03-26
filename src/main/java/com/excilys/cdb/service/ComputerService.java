package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;

public class ComputerService {
	private ComputerDao computerDao;
	
	public ComputerService(DaoFactory daoFactory) {
		this.computerDao = daoFactory.getComputerDao();
	}
	
	public ArrayList<Computer> listService() throws DaoException {
		return this.computerDao.list();
	}
	
	public Optional<Computer> findService(int id) throws DaoException {
		return this.computerDao.findById(id);
	}
	
	public void createService(Computer computer) throws DaoException, DaoException, ComputerNullNameException, DiscontinuedButNoIntroducedException, DiscontinuedBeforeIntroducedException {
		this.computerDao.create(computer);
	}
	
	public void updateService(Computer computer) throws DaoException, DaoException, ComputerNullNameException, DiscontinuedButNoIntroducedException, DiscontinuedBeforeIntroducedException {
		this.computerDao.update(computer);
	}

}
