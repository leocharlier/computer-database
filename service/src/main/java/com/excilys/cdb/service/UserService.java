package com.excilys.cdb.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDao;

@Lazy
@Service
public class UserService {
	private UserDao userDao;
	
	public UserService(UserDao ud) {
		userDao = ud;
	}
	
	public void createService(User user) throws DaoException {
		this.userDao.create(user);
	}
}
