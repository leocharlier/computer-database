package com.excilys.cdb.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.EmptyPasswordException;
import com.excilys.cdb.exception.EmptyUsernameException;
import com.excilys.cdb.exception.ExistingUserException;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.UserService;
import com.excilys.cdb.validator.UserValidator;

@Controller
public class UserController {
	private static final String LOGIN = "login";
	private static final String ADD_USER = "addUser";
	
	private UserService userService;
	
	private UserValidator userValidator;
	
	public UserController(UserService us, UserValidator uv) {
		userService = us;
		userValidator = uv;
	}
	
	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}
	
	@ModelAttribute
	public User initUser() {
		return new User();
	}
	
	@GetMapping({"/","/login"})
	public String getLoginPage(@RequestParam(required = false) Map<String, String> paths, Model model) {
		if(paths.containsKey("error")) {
			model.addAttribute("errorLogin", true);
		}
		return LOGIN;
	}
	
	@PostMapping("/registration")
	public ModelAndView postUserRegistration(@Validated @ModelAttribute("user")User user, BindingResult result, Model model) {
		if(result.hasErrors()) {
			ModelAndView errorView = new ModelAndView(LOGIN);
			for(ObjectError error : result.getAllErrors()) {
				errorView.addObject("errorUsername", error.getDefaultMessage());
			}
			errorView.addObject("user", new User());
			return errorView;
		}

		try {
			this.userService.registerService(user);
			return new ModelAndView("redirect:/dashboard");
		} catch (DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the creation...");
			return new ModelAndView("redirect:/500");
		}
	}
	
	@PostMapping("/userConnection")
	public ModelAndView postUserConnection(@ModelAttribute("user")User user, Model model) {
		try {
			this.userService.registerService(user);
			return new ModelAndView("redirect:/dashboard");
		} catch (DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the creation...");
			return new ModelAndView("redirect:/500");
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/newUser")
	public ModelAndView getNewUserPage(Model model, Principal principal) {
		model.addAttribute("user", principal.getName());
		return new ModelAndView(ADD_USER);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/addUser")
	public ModelAndView postAddUser(HttpServletRequest request, Model model, Principal principal) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String authority = request.getParameter("authority");
		
		try {
			this.userService.addService(username, password, authority);
			model.addAttribute("successUsername", username);
		} catch(DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the creation...");
			return new ModelAndView("redirect:/500");
		} catch(ExistingUserException e) {
			model.addAttribute("errorUsername", username);
			return getNewUserPage(model, principal);
		} catch(EmptyUsernameException e) {
			System.out.println("Coucou");
			model.addAttribute("incorrectField", "username");
			return getNewUserPage(model, principal);
		} catch(EmptyPasswordException e) {
			model.addAttribute("incorrectField", "password");
			return getNewUserPage(model, principal);
		}
		
		return getNewUserPage(model, principal);
	}
}
