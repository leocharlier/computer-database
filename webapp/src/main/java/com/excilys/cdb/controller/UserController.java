package com.excilys.cdb.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.UserService;

@Controller
public class UserController {
	private static final String WELCOME = "welcome";
	
	private UserService userService;
	
	public UserController(UserService us) {
		userService = us;
	}
	
	@ModelAttribute
	public User initUser() {
		return new User();
	}
	
	@GetMapping("/")
	public String getDashBoard(@RequestParam(required = false) Map<String, String> paths, Model model) {
		return WELCOME;
	}
	
	@PostMapping("/userRegistration")
	public ModelAndView postuserRegistration(@ModelAttribute("user")User user, BindingResult result, Model model) {
		if(result.hasErrors()) {
			StringBuilder sb = new StringBuilder("Error(s) : \n");
			for(ObjectError error : result.getAllErrors()) {
				sb.append("<strong>");
				sb.append(error.getDefaultMessage());
				sb.append("</strong>");
				sb.append("\n");
			}
			model.addAttribute("errorMessage", "An error has occured during the <strong>form validation</strong>... " + sb);
			return new ModelAndView("redirect:/500");
		}

		try {
			this.userService.createService(user);
			return new ModelAndView("redirect:/dashboard");
		} catch (DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the creation...");
			return new ModelAndView("redirect:/500");
		}
	}
}
