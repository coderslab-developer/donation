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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
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
			if(user.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
				dealers = dealerService.findAllByStatusAndArchive(true, false);
				model.addAttribute("dealers", dealers);
			} else if (user.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
				Dealer dealer = dealerService.findByUsernameAndArchive(username, false);
				clients = clientService.findAllByDealerIdAndStatusAndSmsServiceAndArchive(dealer.getDealerId(), true, true, false);
				model.addAttribute("clients", clients);
			} else if (user.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
				Client client = clientService.findByUsername(username);
				model.addAttribute("client", client);
			}
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error("{}", e);
		}

		model.addAttribute("username", username);
		model.addAttribute("smsBucket", list.isEmpty() ? new SMSTransaction() : list.get(0));
		model.addAttribute("transactions", list);
		
		return LOCATION + LOCATION_TO;
	}

	@PostMapping("/automessage")
	public String setAutoMessage(Client client, RedirectAttributes redirect) {
		logger.info("Auto message save for Client : {}", client.getAutoMessage());
		try {
			Client cl = clientService.findByUsername(client.getUsername());
			cl.setAutoMessage(client.getAutoMessage());
			cl.setCampaignName(client.getCampaignName());
			cl.setMaskName(client.getMaskName());
			boolean stat = clientService.save(cl);
			if(Boolean.TRUE.equals(stat)) {
				redirect.addFlashAttribute("sm", "Auto message saved successfully");
			} else {
				redirect.addFlashAttribute("em", "Auto message not saved");
			}
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		return "redirect:/smsBucket/" + client.getUsername(); 
	}
}
