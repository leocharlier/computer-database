package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Computer;

@Lazy
@Repository
public class ComputerDao {
  static final Logger LOGGER = Logger.getLogger(ComputerDao.class);
  private SessionFactory sessionFactory;
  
  public ComputerDao(SessionFactory sf) {
	  sessionFactory = sf;
  }

  private static final String SQL_SELECT_ALL = 
      "FROM Computer";
  private static final String SQL_SEARCH = 
	  "SELECT cpt FROM Computer cpt LEFT OUTER JOIN Company cpn ON cpt.company=cpn WHERE cpt.name LIKE :search OR cpn.name LIKE :search";

  public ArrayList<Computer> list() throws DaoException {
	try(Session session = sessionFactory.openSession()) {
		return (ArrayList<Computer>) session.createQuery(SQL_SELECT_ALL, Computer.class).list();
	} catch(HibernateException e) {
		throw new DaoException("Failed to list the computers.", e.getCause());
	}
  }

  public Optional<Computer> findById(int pid) throws DaoException {
    try(Session session = sessionFactory.openSession()) {
    	Optional<Computer> computerToFind = Optional.ofNullable(session.get(Computer.class, pid));
    	if(computerToFind.isPresent()) {
    		LOGGER.info("Computer " + computerToFind.get().getId() + " found.");
    	} else {
    		LOGGER.warn("The computer " + pid + " doesn't exist.");
    	}
    	
    	return computerToFind;
	} catch(HibernateException e) {
		throw new DaoException("Failed to find the company " + pid + ".", e.getCause());
    }
  }

  public void create(Computer computer) throws DaoException {
    try(Session session = sessionFactory.openSession()) {
    	Transaction tx = session.beginTransaction();
    	session.save(computer);
    	tx.commit();
	} catch(HibernateException e) {
		throw new DaoException("Failed to create the computer '" + computer.getName() + "'.", e.getCause());
    }
  }

  public void update(Computer computerUpdated) throws DaoException {
    try(Session session = sessionFactory.openSession()) {
    	Transaction tx = session.beginTransaction();
    	computerUpdated = (Computer) session.merge(computerUpdated);
    	session.update(computerUpdated);
    	tx.commit();
	} catch(HibernateException e) {
		throw new DaoException("Failed to update the computer " + computerUpdated.getId() + ".", e.getCause());
    }
  }

  public void delete(Computer computerDeleted) throws DaoException {
	try(Session session = sessionFactory.openSession()) {
		Transaction tx = session.beginTransaction();
		computerDeleted = (Computer) session.merge(computerDeleted);
		session.delete(computerDeleted);
		tx.commit();
	} catch(HibernateException e) {
		throw new DaoException("Failed to delete the computer " + computerDeleted.getId() + ".", e.getCause());
	}
  }
  
  public ArrayList<Computer> search(String search) throws DaoException {
	try(Session session = sessionFactory.openSession()) {
		return (ArrayList<Computer>) session.createQuery(SQL_SEARCH, Computer.class)
				.setParameter("search", "%"+search+"%").list();
	} catch(HibernateException e) {
		throw new DaoException("Failed to create computers list for search '" + search + "'.", e.getCause());
	}
  }
}
