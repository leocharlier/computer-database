package com.excilys.cdb.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.EmptyPasswordException;
import com.excilys.cdb.exception.EmptyUsernameException;
import com.excilys.cdb.exception.ExistingUserException;
import com.excilys.cdb.model.Authority;
import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDao;

@Service
public class UserService implements UserDetailsService {
	private UserDao userDao;
	
	public UserService(UserDao ud) {
		userDao = ud;
	}
	
	public void registerService(User user) throws DaoException {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setUsername(user.getUsername().trim());
		Authority authority = new Authority();
		authority.setUser(user);
		authority.setAuthority("ROLE_USER");
		this.userDao.create(user, authority);
	}
	
	public void addService(String username, String password, String authority) throws DaoException, ExistingUserException, EmptyUsernameException, EmptyPasswordException {
		if("".equals(username)) {
			throw new EmptyUsernameException("The username cannot be null or empty.");
		}
		if("".equals(password)) {
			throw new EmptyPasswordException("The passsword cannot be null or empty.");
		}
		if(this.userDao.findByUsername(username.trim()).isPresent()) {
			throw new ExistingUserException("This username is already used.");
		}
		
		User user = new User();
		user.setUsername(username.trim());
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		Authority userAuthority = new Authority();
		userAuthority.setUser(user);
		userAuthority.setAuthority(authority);
		this.userDao.create(user, userAuthority);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userDao.findByUsername(username);
	    UserBuilder builder = null;
	    
	    if (user.isPresent()) {
	      builder = org.springframework.security.core.userdetails.User.withUsername(username);
	      builder.disabled(false);
	      builder.password(user.get().getPassword());
	      String[] authorities = user.get().getAuthorities()
	          .stream().map(a -> a.getAuthority()).toArray(String[]::new);

	      builder.authorities(authorities);
	    } else {
	      throw new UsernameNotFoundException("User not found.");
	    }
	    return builder.build();
	}
}
