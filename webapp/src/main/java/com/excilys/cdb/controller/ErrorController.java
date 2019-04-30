package com.excilys.cdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	private static final String EXCEPTION_VIEW = "500";
	private static final String NOT_FOUND_VIEW = "404";
	private static final String PERMISSION_DENIED_VIEW = "403";
	
	@GetMapping("/404")
	public String get404(Model model) {
		return NOT_FOUND_VIEW;
	}
	
	@GetMapping("/500")
	public String get500(Model model) {
		return EXCEPTION_VIEW;
	}
	
	@GetMapping("/403")
	public String get403(Model model) {
		return PERMISSION_DENIED_VIEW;
	}
}
