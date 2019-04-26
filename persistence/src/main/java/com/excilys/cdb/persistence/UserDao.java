package com.excilys.cdb.persistence;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.User;

@Lazy
@Repository
public class UserDao {
	static final Logger LOGGER = Logger.getLogger(UserDao.class);
	private SessionFactory sessionFactory;
	  
	public UserDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public void create(User user) throws DaoException {
		try(Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch(HibernateException e) {
			throw new DaoException("Failed to create the user '" + user.getUsername() + "'.", e.getCause());
		}
	}
}
