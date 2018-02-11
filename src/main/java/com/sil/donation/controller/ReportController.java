package com.sil.donation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController {

	private static final String PAGE_TITLE = "Report";
	//private static final String REDIRECT = "redirect:/";
	//private static final String REDIRECT_TO = "report";
	private static final String LOCATION = "views/report/";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
	
	@RequestMapping
	public String loadReportPage(Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + "report";
	}
	
}
