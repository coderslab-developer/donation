package com.sil.donation.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Donar;
import com.sil.donation.entity.Donation;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.DonationService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/donation")
public class DonationController {

	private static final String PAGE_TITLE = "Add donation";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "donation";
	private static final String LOCATION_TO = "add_donation";
	private static final String LOCATION = "views/donation/";
	private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

	@Autowired private DonationService donationService;
	@Autowired private DonarService donarService;
	@Autowired private ClientService clientService;

	@RequestMapping
	public String addDonation(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		model.addAttribute("pageTitle", PAGE_TITLE);
		Donation donation = new Donation();
		try {
			donation.setDonars(donarService.findAllByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		model.addAttribute("donation", donation);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(Donation donation, RedirectAttributes redirect) {
		if(donation.getDonarId() <= 0 || donation.getPaid() <= 0) {
			redirect.addFlashAttribute("em", "Donation not received");
			return REDIRECT + REDIRECT_TO;
		}
		Donar donar = null;
		try {
			donar = donarService.findByDonarId(donation.getDonarId(), false);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		donation.setPayableAmount(donar.getPayableAmount());
		donation.setDue(donation.getPayableAmount() - donation.getPaid());
		donation.setPayDate(new Date());
		donationService.save(donation);
		redirect.addFlashAttribute("sm", "Donation received successfully");
		return REDIRECT + REDIRECT_TO;
	}

}
