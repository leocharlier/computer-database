package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;

@Lazy
@Repository
public class CompanyDao {
  static final Logger LOGGER = Logger.getLogger(CompanyDao.class);
  private SessionFactory sessionFactory;
  
  public CompanyDao(SessionFactory sf) {
	  sessionFactory = sf;
  }
  
  private static final String SQL_SELECT_ALL      = "FROM Company";
  private static final String SQL_SELECT_BY_NAME  = "FROM Company WHERE name = :companyName";
  private static final String SQL_DELETE_COMPUTER = "DELETE FROM Computer WHERE company = :company";
  
  public ArrayList<Company> list() throws DaoException {
    try(Session session = sessionFactory.openSession()) {
    	return (ArrayList<Company>) session.createQuery(SQL_SELECT_ALL, Company.class).list();
	} catch(HibernateException e) {
		throw new DaoException("Failed to list the companies.", e.getCause());
	}
  }

  public Optional<Company> findById(int pid) throws DaoException {
    try(Session session = sessionFactory.openSession()) {
    	return Optional.ofNullable(session.get(Company.class, pid));
	} catch(HibernateException e) {
		throw new DaoException("Failed to find the company " + pid + ".", e.getCause());
	}
  }
  
  public Optional<Company> findByName(String pname) throws DaoException{
    Optional<Company> company;
    try(Session session = sessionFactory.openSession()) {
		company = Optional.of(session.createQuery(SQL_SELECT_BY_NAME, Company.class).setParameter("companyName", pname).getSingleResult());
	} catch(NoResultException e) {
		company = Optional.empty();
	} catch(HibernateException e) {
		throw new DaoException("Failed to find the company '" + pname + "'.", e.getCause());
	}
	return company;
  }
  
  public void delete(Company companyDeleted) throws DaoException {
	try(Session session = sessionFactory.openSession()) {
	  Transaction tx = session.beginTransaction();
	  try {
		int computersDeleted = session.createQuery(SQL_DELETE_COMPUTER).setParameter("company", companyDeleted).executeUpdate();
		session.delete(companyDeleted);
		tx.commit();
		LOGGER.info(computersDeleted + " computers deleted.");
		LOGGER.info("Company " + companyDeleted.getId() + " deleted.");
	  } catch(HibernateException e) {
		tx.rollback();
		throw new DaoException("Failed to delete the company " + companyDeleted.getId() + ".", e.getCause());
	  }   
	} catch(HibernateException e) {
		throw new DaoException("Failed to delete the company " + companyDeleted.getId() + ".", e.getCause());
    }
  }
}
