package com.sil.donation.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.AdminDashboard;
import com.sil.donation.model.ClientDashboard;
import com.sil.donation.model.DealerDashboard;
import com.sil.donation.model.UserRole;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping({"/", "/home"})
public class DashboardController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	private static final String PAGE_TITLE = "Dashboard";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO_ADMIN = "admin_dashboard";
	private static final String REDIRECT_TO_DEALER = "dealer_dashboard";
	private static final String REDIRECT_TO_CLIENT = "client_dashboard";
	private static final String LOCATION = "views/dashboard/";

	@Autowired private DealerService dealerService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;

	@RequestMapping
	public String loadHomePage(Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		String role = null;
		for(String r : roles) {
			role = r;
		}

		if(role.equalsIgnoreCase(UserRole.ROLE_ADMIN.name())) {
			Map<String, Object> map = getAllDealerAndClientInfo();
			try {
				model.addAttribute("adminDashboard", getAdminDashBoardInfo());
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
			model.addAttribute("clients", map.get("clients"));
			model.addAttribute("dealers", map.get("dealers"));
			return LOCATION + REDIRECT_TO_ADMIN;
		}else if(role.equalsIgnoreCase(UserRole.ROLE_DEALER.name())) {
			Map<String, Object> map = getAllDealersInfo(authentication.getName());
			model.addAttribute("clients", map.get("clients"));
			try {
				model.addAttribute("dealerDashboard", getDealerDashboardInfo(authentication.getName()));
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
			return LOCATION + REDIRECT_TO_DEALER;
		}else if(role.equalsIgnoreCase(UserRole.ROLE_CLIENT.name())){
			try {
				model.addAttribute("clientDashboard", getClientDashboardInfo(authentication.getName()));
				model.addAttribute("donars", getAllDonarsInfo(authentication.getName()).get("donars"));
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
			return LOCATION + REDIRECT_TO_CLIENT;
		}else {
			return REDIRECT + "login";
		}
	}

	public Map<String, Object> getAllDonarsInfo(String username) throws SilException{
		Map<String, Object> map = new HashMap<>();
		List<Donar> donars = donarService.findAllByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false);
		donars.stream().forEach(d -> {
			try {
				d.setCategoryName(categoryService.findByCategoryIdAndArchive(d.getCategoryId(), false).getName());
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
		});
		map.put("donars", donars);
		return map;
	}

	public DealerDashboard getDealerDashboardInfo(String username) throws SilException {
		Dealer dealer = null;
		try {
			dealer = dealerService.findByUsernameAndArchive(username, false);
		} catch (Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
		DealerDashboard dealerDashboard = new DealerDashboard();
		dealerDashboard.setActiveClient(clientService.findByDealerIdAndStatusAndArchive(dealer.getDealerId(), true, false).size());
		dealerDashboard.setInactiveClient(clientService.findByDealerIdAndStatusAndArchive(dealer.getDealerId(), false, false).size());
		dealerDashboard.setTotalSellOfSoftware(clientService.findByDealerIdAndArchive(dealer.getDealerId(), false).size());
		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clientService.findByDealerIdAndArchive(dealer.getDealerId(), false)) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
				if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
					renewalClients.add(c);
				}
			}
		}
		dealerDashboard.setServiceRenewOnThisMonth(renewalClients.size());
		
		return dealerDashboard;
	}

	public Map<String, Object> getAllDealersInfo(String username){
		Map<String, Object> map = new HashMap<>();
		try {
			List<Client> clients = clientService.findByDealerIdAndArchive(dealerService.findByUsernameAndArchive(username, false).getDealerId(), false);
			map.put("clients", clients);
		} catch (Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
		return map;
	}

	public Map<String, Object> getAllDealerAndClientInfo() {
		Map<String, Object> map = new HashMap<>();

		List<Dealer> dealers = null;
		List<Client> clients = null;
		try {
			dealers = dealerService.findAllByArchive(false);
			clients = clientService.findAllByArchive(false);
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}

		dealers.stream().forEach(d -> {
			try {
				d.setClients(clientService.findByDealerIdAndArchive(d.getDealerId(), false));
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
		});
		dealers.stream().forEach(d -> {
			try {
				d.setActiveClients(clientService.findByDealerIdAndStatusAndArchive(d.getDealerId(), true, false).size());
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
		});
		dealers.stream().forEach(d -> {
			try {
				d.setInactiveClients(clientService.findByDealerIdAndStatusAndArchive(d.getDealerId(), false, false).size());
			} catch (SilException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
		});
		map.put("dealers", dealers.size() > 0 ? dealers : null);

		clients.stream().forEach(c -> {
			try {
				c.setDealerName(dealerService.findByDealerIdAndArchive(c.getDealerId(), false).getDealerName());
				long remainingDays = c.getExpireDate().getTime() - new Date().getTime();
				c.setRemainingDay(String.valueOf(remainingDays / (24* 1000 * 60 * 60)));
			} catch (Exception e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			}
		});
		map.put("clients", clients.size() > 0 ? clients : null);
		return map;
	}

	public AdminDashboard getAdminDashBoardInfo() throws SilException {
		AdminDashboard adminDashboard = new AdminDashboard();
		adminDashboard.setActiveClient(clientService.findByStatusAndArchive(true, false).size());
		adminDashboard.setInactiveClients(clientService.findByStatusAndArchive(false, false).size());
		adminDashboard.setTotalDealerOfSoftware(dealerService.findAllByArchive(false).size());
		adminDashboard.setTotalSellOfSoftware(dealerService.findAllByArchive(false).size() + clientService.findAllByArchive(false).size());
		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clientService.findAllByArchive(false)) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
				renewalClients.add(c);
			}
		}
		adminDashboard.setServiceRenewCurrentMotnh(renewalClients.size());
		return adminDashboard;
	}

	public ClientDashboard getClientDashboardInfo(String username) throws SilException {
		ClientDashboard clientDashboard = new ClientDashboard();
		List<Donar> donars = donarService.findAllByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false);
		clientDashboard.setTotalDonar(donars.size());
		clientDashboard.setActiveDonar(donars.stream().filter(d -> Boolean.TRUE == d.isStatus()).collect(Collectors.toList()).size());
		clientDashboard.setInactiveDonar(donars.stream().filter(d -> Boolean.FALSE == d.isStatus()).collect(Collectors.toList()).size());
		clientDashboard.setNumberOfPayeeDonarInThisMonth(0);
		return clientDashboard;
	}

}
