package com.sil.donation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/access-denied")
public class AccessDeniedController {

	private static final Logger logger = LoggerFactory.getLogger(AccessDeniedController.class);
	private static final String PAGE_TITLE = "Access Denied";
	private static final String REDIRECT_TO = "access-denied";
	private static final String LOCATION = "views/access-denied/";

	@RequestMapping
	public String accesDenide(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		model.addAttribute("pageTitle", PAGE_TITLE);
		model.addAttribute("accessMessage", "Access Denied for "+ username.toUpperCase() +". You have no permission to access this page");
		if(logger.isDebugEnabled()) logger.debug("Access Denide for {} user", username);
		return LOCATION + REDIRECT_TO;
	}
}
