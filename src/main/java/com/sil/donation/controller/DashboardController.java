package com.sil.donation.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SMSNotifier;
import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.SMSNotifierService;
import com.sil.donation.service.SiteConfigService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping({"/", "/home"})
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO_ADMIN = "admin_dashboard";
	private static final String REDIRECT_TO_DEALER = "dealer_dashboard";
	private static final String REDIRECT_TO_CLIENT = "client_dashboard";
	private static final String LOCATION = "views/dashboard/";

	@Autowired private DealerService dealerService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;
	@Autowired private SMSNotifierService smsNotifierService;
	@Autowired private SiteConfigService siteConfigService;

	@RequestMapping
	public String loadHomePage(Model model, HttpSession session) throws IllegalAccessException {

		//get Authentication info
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		String role = null;
		for(String r : roles) {
			role = r;
		}
		if(role == null) {
			throw new IllegalAccessException();
		}

		//site configuration
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(authentication.getName());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(siteConfig == null) {
			siteConfig = new SiteConfig();
			siteConfig.setUsername(authentication.getName());
			siteConfig.setEnableLogo(true);
		}
		session.setAttribute("siteConfig", siteConfig);

		//logic for view page
		if(role.equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
			Map<String, Object> map = getAdminDashboardInfo();
			model.addAttribute("adminDashboard", map.get("admin"));
			model.addAttribute("clients", map.get("clients"));
			model.addAttribute("dealers", map.get("dealers"));
			return LOCATION + REDIRECT_TO_ADMIN;
		}else if(role.equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
			Map<String, Object> map = getDealerDashboardInfo(authentication.getName());
			model.addAttribute("dealer", map.get("dealer"));
			model.addAttribute("dealerDashboard", map.get("dealer"));
			return LOCATION + REDIRECT_TO_DEALER;
		}else if(role.equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())){
			Client client = getClientDashboardInfo(authentication.getName());
			model.addAttribute("clientDashboard", client);
			model.addAttribute("client", client);
			model.addAttribute("smsNotifier", getSMSNotification(authentication.getName()));
			return LOCATION + REDIRECT_TO_CLIENT;
		}else {
			return REDIRECT + "login";
		}
	}

	/*
	 * Admin Dashboard info
	 */
	public Map<String, Object> getAdminDashboardInfo() {
		Map<String, Object> map = new HashMap<>();

		List<Dealer> dealers = new ArrayList<>();
		List<Client> clients = new ArrayList<>();
		try {
			dealers = dealerService.findAllByArchive(false);
			clients = clientService.findAllByArchive(false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		/*
		 * Set clients into dealer object by dealerId
		 */
		for(Dealer d : dealers) {
			d.setClients(clients.stream().filter(c -> c.getDealerId() == d.getDealerId()).collect(Collectors.toList()));
			d.setActiveClients(clients.stream().filter(c -> c.getDealerId() == d.getDealerId()).filter(c -> Boolean.TRUE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
			d.setInactiveClients(clients.stream().filter(c -> c.getDealerId() == d.getDealerId()).filter(c -> Boolean.FALSE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		}
		map.put("dealers", dealers);

		/*
		 * Set dealer name for each client and set remaining day
		 */
		for(Client c : clients) {
			Dealer dealer = dealers.stream().filter(d -> d.getDealerId() == c.getDealerId()).collect(Collectors.toList()).stream().findFirst().orElse(null);
			c.setDealerName(dealer == null ? "" : dealer.getDealerName());
			c.setRemainingDay(String.valueOf((c.getExpireDate().getTime() - new Date().getTime()) / (24* 1000 * 60 * 60)));
		}
		map.put("clients", clients);

		/*
		 * Get admin software related info
		 */
		Admin admin = new Admin();
		int totalDealers = dealers.stream().filter(d -> Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size();
		int totalClients = clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size();
		admin.setTotalSellOfSoftware(totalDealers + totalClients);
		admin.setActiveClient(clients.stream().filter(c -> Boolean.TRUE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		admin.setInactiveClients(clients.stream().filter(c -> Boolean.FALSE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		admin.setTotalDealerOfSoftware(dealers.stream().filter(d -> Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());

		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
				renewalClients.add(c);
			}
		}
		admin.setServiceRenewCurrentMotnh(renewalClients.size());
		map.put("admin", admin);

		return map;
	}

	/*
	 * Dealer Dashboard Info
	 */
	public Map<String, Object> getDealerDashboardInfo(String username) {
		Map<String, Object> map = new HashMap<>();
		Dealer dealer = new Dealer();
		List<Client> clients = new ArrayList<>();
		try {
			dealer = dealerService.findByUsernameAndArchive(username, false);
			clients = clientService.findAllByDealerId(dealer.getDealerId());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		dealer.setClients(clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()));
		dealer.setActiveClients(clients.stream().filter(c -> Boolean.TRUE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		dealer.setInactiveClients(clients.stream().filter(c -> Boolean.FALSE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		dealer.setTotalSellOfSoftware(clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
				renewalClients.add(c);
			}
		}
		dealer.setServiceRenewOnThisMonth(renewalClients.size());
		map.put("dealer", dealer);
		return map;
	}

	/*
	 * Client Dashboard Info
	 */
	public Client getClientDashboardInfo(String username) {
		Client client = new Client();
		List<Donar> donars = new ArrayList<>();
		try {
			client = clientService.findByUsernameAndArchive(username, false);
			donars = donarService.findAllByClientIdAndArchive(client.getClientId(), false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(!donars.isEmpty()) {
			for(Donar d : donars) {
				try {
					d.setCategoryName(categoryService.findByCategoryIdAndArchive(d.getCategoryId(), false).getName());
				} catch (SilException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		client.setDonars(donars);
		client.setTotalDonar(donars.size());
		client.setActiveDonar(donars.stream().filter(d -> Boolean.TRUE == d.isStatus()).collect(Collectors.toList()).size());
		client.setInactiveDonar(donars.stream().filter(d -> Boolean.FALSE == d.isStatus()).collect(Collectors.toList()).size());
		client.setNumberOfPayeeDonarInThisMonth(0);
		return client;
	}

	public SMSNotifier getSMSNotification(String username){
		List<SMSNotifier> smsNotifier = new ArrayList<>();
		try {
			smsNotifier = smsNotifierService.findByUsernameAndStatusOrderByIdDesc(username, true);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		return !smsNotifier.isEmpty() ? smsNotifier.get(0) : new SMSNotifier();
	}
}
