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

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	private static final String PAGE_TITLE = "Report";
	private static final String LOCATION = "views/report/";
	
	@RequestMapping
	public String loadReportPage(Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + "report";
	}
	
}
