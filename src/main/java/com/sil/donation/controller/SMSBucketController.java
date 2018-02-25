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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserRole;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.SMSTransactionService;
import com.sil.donation.service.UsersService;

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
	@Autowired ClientService clientService;
	@Autowired UsersService usersService;

	@RequestMapping("/{username}")
	public String smsBucket(@PathVariable("username") String username, Model model, RedirectAttributes redirect) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		
		Users user = null;
		try {
			user = usersService.findByUsernameAndArchive(username, false);
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error("{}", e);
		}

		if(user == null) {
			redirect.addFlashAttribute("em", "No user found");
			return "redirect:/";
		}

		List<SMSTransaction> list = null;
		List<Dealer> dealers = null;
		List<Client> clients = null;
		try {
			list = smsTransactionService.findByUsernameOrderByIdDesc(username);
			if(user.getAuthority().equalsIgnoreCase(UserRole.ROLE_ADMIN.name())) {
				dealers = dealerService.findAllByStatusAndArchive(true, false);
				model.addAttribute("dealers", dealers);
			}else if(user.getAuthority().equalsIgnoreCase(UserRole.ROLE_DEALER.name())) {
				Dealer dealer = dealerService.findByUsernameAndArchive(username, false);
				clients = clientService.findAllByDealerIdAndStatusAndSmsServiceAndArchive(dealer.getDealerId(), true, true, false);
				model.addAttribute("clients", clients);
			}
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error("{}", e);
		}

		model.addAttribute("username", username);
		model.addAttribute("smsBucket", list.isEmpty() ? new SMSTransaction() : list.get(0));
		return LOCATION + LOCATION_TO;
	}

}
