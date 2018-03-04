package com.sil.donation.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

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
	public String loadLoginPage(Model model, HttpSession session) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		session.setAttribute("lastLogin", String.valueOf(new Date()));
		logger.info("Login page called at {}", new Date());
		return LOCATION + REDIRECT_TO;
	}

}
