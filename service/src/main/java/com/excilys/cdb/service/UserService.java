package com.excilys.cdb.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDao;

@Lazy
@Service
public class UserService {
	private PasswordEncoder passwordEncoder;
	private UserDao userDao;
	
	public UserService(UserDao ud, PasswordEncoder pe) {
		userDao = ud;
		passwordEncoder = pe;
	}
	
	public void registerService(User user) throws DaoException {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		this.userDao.create(user);
	}
}
