package com.excilys.cdb.persistence;

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
import com.excilys.cdb.model.Authority;
import com.excilys.cdb.model.User;

@Lazy
@Repository
public class UserDao {
	static final Logger LOGGER = Logger.getLogger(UserDao.class);
	private SessionFactory sessionFactory;
	
	private static final String SQL_SELECT_BY_NAME  = "FROM User WHERE username = :username";
	
	public UserDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public void create(User user) throws DaoException {
		try(Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.save(user);
			Authority authority = new Authority();
			authority.setUser(user);
			authority.setAuthority("ROLE_USER");
			session.save(authority);
			tx.commit();
		} catch(HibernateException e) {
			throw new DaoException("Failed to create the user '" + user.getUsername() + "'.", e.getCause());
		}
	}
	
	public Optional<User> findByUsername(String pusername) throws DaoException{
	    Optional<User> user;
	    try(Session session = sessionFactory.openSession()) {
	    	user = Optional.of(session.createQuery(SQL_SELECT_BY_NAME, User.class).setParameter("username", pusername).getSingleResult());
		} catch(NoResultException e) {
			user = Optional.empty();
		} catch(HibernateException e) {
			throw new DaoException("Failed to find the user '" + pusername + "'.", e.getCause());
		}
		return user;
	}
}
