package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;

public class ComputerService {
	private ComputerDao computerDao;
	
	private static Comparator<Computer> introducedComparator = (computer1, computer2) -> {
		if(!computer1.getIntroduced().isPresent() && !computer2.getIntroduced().isPresent()) {
			return 0;
		}
		if(!computer1.getIntroduced().isPresent()) {
			return 1;
		}
		if(!computer2.getIntroduced().isPresent()) {
			return -1;
		}
		return Long.compare(computer1.getIntroduced().get().getTime(), computer2.getIntroduced().get().getTime());
	};
	
	private static Comparator<Computer> discontinuedComparator = (computer1, computer2) -> {
		if(!computer1.getDiscontinued().isPresent() && !computer2.getDiscontinued().isPresent()) {
			return 0;
		}
		if(!computer1.getDiscontinued().isPresent()) {
			return 1;
		}
		if(!computer2.getDiscontinued().isPresent()) {
			return -1;
		}
		return Long.compare(computer1.getDiscontinued().get().getTime(), computer2.getDiscontinued().get().getTime());
	};
	
	private static Comparator<Computer> companyComparator = (computer1, computer2) -> {
		if(!computer1.getCompany().isPresent() && !computer2.getCompany().isPresent()) {
			return 0;
		}
		if(!computer1.getCompany().isPresent()) {
			return 1;
		}
		if(!computer2.getCompany().isPresent()) {
			return -1;
		}
		return computer1.getCompany().get().getName().compareTo(computer2.getCompany().get().getName());
	};
	
	public ComputerService(DaoFactory daoFactory) {
		this.computerDao = daoFactory.getComputerDao();
	}
	
	public ArrayList<Computer> listService() throws DaoException {
		return this.computerDao.list();
	}
	
	public Optional<Computer> findService(int id) throws DaoException {
		return this.computerDao.findById(id);
	}
	
	public void createService(Computer computer) throws DaoException, ComputerNullNameException, DiscontinuedButNoIntroducedException, DiscontinuedBeforeIntroducedException {
		this.checkData(computer);
		this.computerDao.create(computer);
	}
	
	public void updateService(Computer computer) throws DaoException, ComputerNullNameException, DiscontinuedButNoIntroducedException, DiscontinuedBeforeIntroducedException {
		this.checkData(computer);
		this.computerDao.update(computer);
	}
	
	public void deleteService(Computer computer) throws DaoException {
		this.computerDao.delete(computer);
	}
	
	public ArrayList<Computer> searchService(String search) throws DaoException {
		return this.computerDao.search(search);
	}
	
	public void sortByNameAscService(ArrayList<Computer> computers) {
		computers.sort((computer1, computer2) -> computer1.getName().toLowerCase().compareTo(computer2.getName().toLowerCase()));
	}
	
	public void sortByNameDescService(ArrayList<Computer> computers) {
		computers.sort((computer1, computer2) -> computer2.getName().toLowerCase().compareTo(computer1.getName().toLowerCase()));
	}
	
	public void sortByIntroducedAscService(ArrayList<Computer> computers) {
		computers.sort(introducedComparator);
	}
	
	public void sortByIntroducedDescService(ArrayList<Computer> computers) {
		computers.sort(introducedComparator.reversed());
		List<Computer> nullIntroducedComputers = computers.stream().filter(computer -> !computer.getIntroduced().isPresent()).collect(Collectors.toList());
		computers.removeIf(computer -> !computer.getIntroduced().isPresent());
		computers.addAll(nullIntroducedComputers);
	}
	
	public void sortByDiscontinuedAscService(ArrayList<Computer> computers) {
		computers.sort(discontinuedComparator);
	}
	
	public void sortByDiscontinuedDescService(ArrayList<Computer> computers) {
		computers.sort(discontinuedComparator.reversed());
		List<Computer> nullDiscontinuedComputers = computers.stream().filter(computer -> !computer.getDiscontinued().isPresent()).collect(Collectors.toList());
		computers.removeIf(computer -> !computer.getDiscontinued().isPresent());
		computers.addAll(nullDiscontinuedComputers);
	}
	
	public void sortByCompanyNameAscService(ArrayList<Computer> computers) {
		computers.sort(companyComparator);
	}
	
	public void sortByCompanyNameDescService(ArrayList<Computer> computers) {
		computers.sort(companyComparator.reversed());
		List<Computer> nullCompanyComputers = computers.stream().filter(computer -> !computer.getCompany().isPresent()).collect(Collectors.toList());
		computers.removeIf(computer -> !computer.getCompany().isPresent());
		computers.addAll(nullCompanyComputers);
	}
	
	private void checkData(Computer computer) {
	    if (computer.getName() == null || computer.getName().trim().isEmpty()) {
	      throw new ComputerNullNameException("Failed to create/update computer : Computer name is null or empty.");
	    }

	    if (!computer.getIntroduced().isPresent() && computer.getDiscontinued().isPresent()) {
	      throw new DiscontinuedButNoIntroducedException("Failed to create/update computer : Dicontinued not null whereas introcued is.");
	    }

	    if ( computer.getIntroduced().isPresent() && computer.getDiscontinued().isPresent()
	        && !computer.getDiscontinued().get().after(computer.getIntroduced().get())){
	      throw new DiscontinuedBeforeIntroducedException("Failed to create/update computer : Discontinuation date is not after the introduction date.");
	    }
	  }
}
