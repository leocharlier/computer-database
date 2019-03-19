package com.excilys.cdb.persistence;

import com.excilys.cdb.model.Computer;

import java.util.ArrayList;

public interface ComputerDao {
  ArrayList<Computer> list() throws DaoException;
  
  Computer find(int pid) throws DaoException;
  
  void create(Computer computer) throws DaoException;
  
  void update(Computer computer) throws DaoException;

  void delete(Computer computer) throws DaoException;

}
