package com.sil.donation.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donation;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.DonationService;
import com.sil.donation.service.UsersService;

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
	private static final String LOCATION_TO = "report";
	private static final String REDIRECT = "redirect:/";
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired private DonationService donationService;
	@Autowired private DonarService donarService;
	@Autowired private ClientService clientService;
	@Autowired private UsersService usersService;
	@Autowired private DealerService dealerService;

	@RequestMapping
	public String loadReportPage(Model model, RedirectAttributes redirect) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Users users = null;
		try {
			users = usersService.findByUsernameAndArchive(username, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(users == null) {
			redirect.addFlashAttribute("em", "You have not permission for this page");
			return REDIRECT;
		}

		try {
			if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
				Client client = clientService.findByUsernameAndArchive(username, false);
				model.addAttribute("donars", donarService.findAllByClientIdAndArchive(client.getClientId(), false));
			}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
				Dealer dealer = dealerService.findByUsernameAndArchive(username, false);
				model.addAttribute("clients", clientService.findByDealerIdAndArchive(dealer.getDealerId(), false));
			}
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return LOCATION + LOCATION_TO;
	}

	@PostMapping("/transactionInfo")
	public @ResponseBody List<Donation> getDonarTransactionInfo(Integer donarId, String startDate, String endDate) throws ParseException {
		if(startDate == "" || endDate == "") {
			return new ArrayList<>();
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Integer clientId = null;
		try {
			clientId = clientService.findByUsernameAndArchive(username, false).getClientId();
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		Date firstDate = DATE_FORMATTER.parse(startDate);
		Date lastDate = DATE_FORMATTER.parse(endDate);

		if(firstDate.compareTo(lastDate) > 0) {
			firstDate = DATE_FORMATTER.parse(endDate);
			lastDate = DATE_FORMATTER.parse(startDate);
		}

		List<Donation> donations = donationService.findAllDonationByClientIdAndPayDateBetweenStartDateToEndDate(clientId, donarId, firstDate, lastDate);
		try {
			for(Donation d : donations) {
				d.setDonarName(donarService.findByDonarId(d.getDonarId(), false).getDonarName());
			}
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return donations.isEmpty() ? new ArrayList<>() : donations;
	}
}
