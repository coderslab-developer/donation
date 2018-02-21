package com.sil.donation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private static final String PAGE_TITLE = "Login";
	private static final String REDIRECT_TO = "login";
	private static final String LOCATION = "views/login/";
	
	@RequestMapping
	public String loadLoginPage(Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		logger.info("Login successfull");
		return LOCATION + REDIRECT_TO;
	}
	
}
