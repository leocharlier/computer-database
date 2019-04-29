package com.excilys.cdb.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
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
		this.userDao.create(user);
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
