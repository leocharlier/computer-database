package com.excilys.cdb.validator;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDao;

@Component
public class UserValidator implements Validator {
	static final Logger LOGGER = Logger.getLogger(UserValidator.class);
	private UserDao userDao;
	
	public UserValidator(UserDao ud) {
		userDao = ud;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username field must be set.");
		ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Password field must be set.");
		
		User user = (User) target;
		if(this.userDao.findByUsername(user.getUsername()).isPresent()) {
			LOGGER.warn("Cannot create the account because " + user.getUsername() + " is already used.");
			errors.rejectValue("username", "usernameexists", user.getUsername());
		}
	}
}
