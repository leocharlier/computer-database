package com.excilys.persistence;

import java.util.ArrayList;

import com.excilys.model.Computer;

public interface ComputerDAO {
	
	ArrayList<Computer> list() throws DAOException;
	
	Computer find( int pId ) throws DAOException;
	
	void create( Computer computer ) throws DAOException;
	
	void update( Computer computer ) throws DAOException;
	
	void delete( Computer computer ) throws DAOException;
	
}
