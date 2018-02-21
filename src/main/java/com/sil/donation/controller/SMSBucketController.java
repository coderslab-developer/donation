/**
 * 
 */
package com.sil.donation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.SMSTransactionService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/smsBucket")
public class SMSBucketController {

	private static final Logger logger = LoggerFactory.getLogger(SMSBucketController.class);
	private static final String PAGE_TITLE = "SMS Bucket";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "smsBucket";
	private static final String LOCATION_TO = "sms_bucket";
	private static final String LOCATION = "views/sms-bucket/";

	@Autowired SMSTransactionService smsTransactionService;
	@Autowired DealerService dealerService;

	@GetMapping("/{username}")
	public String smsBucket(@PathVariable("username") String username, Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);

		List<SMSTransaction> list = null;
		List<Dealer> dealers = null;
		try {
			list = smsTransactionService.findByUsernameOrderByUsernameDesc(username);
			dealers = dealerService.findAllByStatusAndArchive(true, false);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		model.addAttribute("username", username);
		model.addAttribute("dealers", dealers);
		model.addAttribute("smsBucket", list.isEmpty() ? new SMSTransaction() : list.get(0));
		return LOCATION + LOCATION_TO;
	}

}
