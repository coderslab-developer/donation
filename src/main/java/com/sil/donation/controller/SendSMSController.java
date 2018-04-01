/**
 * 
 */
package com.sil.donation.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.service.AdminService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.SMSTransactionService;
import com.sil.donation.service.UsersService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/sendSms")
public class SendSMSController {

	private static final Logger logger = LoggerFactory.getLogger(SendSMSController.class);

	private static final String PAGE_TITLE = "Send SMS";
	private static final String LOCATION_TO = "send_sms";
	private static final String LOCATION = "views/send-sms/";

	@Autowired private UsersService usersService;
	@Autowired private SMSTransactionService smsTransactionService;
	@Autowired private DonarService donarService;
	@Autowired private ClientService clientService;
	@Autowired private AdminService adminService;
	@Autowired private DealerService dealerService;

	@RequestMapping("/{username}")
	public String loadSmsPage(@PathVariable("username") String username, Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);

		List<SMSTransaction> smsTransactions = new ArrayList<>();
		try {
			smsTransactions = smsTransactionService.findByUsernameOrderByIdDesc(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		int availableSMS = 0;
		if(!smsTransactions.isEmpty()) {
			availableSMS = smsTransactions.get(0).getAvailableSMS();
		}

		Users users = null;
		try {
			users = usersService.findByUsernameAndArchive(username, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		String authority = null;
		if(users != null) {
			authority = users.getAuthority();
		}

		if(authority != null) {
			try {
				if(authority.equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
					Admin admin = adminService.findByUsernameAndArchive(username, false);
					List<Dealer> dealers = dealerService.findAllByAdminIdAndStatusAndArchive(admin.getAdminId(), true, false);
					model.addAttribute("dealers", dealers);
				}else if(authority.equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
					Dealer dealer = dealerService.findByUsernameAndArchive(username, false);
					List<Client> clients = clientService.findByDealerIdAndStatusAndArchive(dealer.getDealerId(), true, false);
					model.addAttribute("clients", clients);
				}else if(authority.equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
					Client client = clientService.findByUsernameAndArchive(username, false);
					List<Donar> donars = donarService.findAllByClientIdAndStatusAndArchive(client.getClientId(), true, false);
					model.addAttribute("donars", donars);
				}
			} catch (SilException e) {
				logger.error(e.getMessage(), e);
			}
			
		}

		model.addAttribute("availableSMS", availableSMS);
		return LOCATION + LOCATION_TO;
	}
}
