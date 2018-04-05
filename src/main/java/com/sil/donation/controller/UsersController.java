package com.sil.donation.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.ResponseMessage;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.service.AdminService;
import com.sil.donation.service.AuthoritiesService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.UsersService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/users")
public class UsersController {

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

	private static final String PAGE_TITLE = "Users";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "users";
	private static final String LOCATION_TO = "users";
	private static final String LOCATION = "views/users/";

	@Autowired private UsersService usersService;
	@Autowired private AdminService adminService;
	@Autowired private DealerService dealerService;
	@Autowired private ClientService clientService;
	@Autowired private AuthoritiesService authoritiesService;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping
	public String createAndSaveUser(@ModelAttribute("users") @Valid Users users, BindingResult result, RedirectAttributes redirect, Model model) {
	
		if(result.hasErrors()) {
			model.addAttribute("pageTitle", "Create User");
			Map<String, String> roles = new HashMap<>();
			roles.put(UserAuthorities.ROLE_ADMIN.code(), UserAuthorities.ROLE_ADMIN.name());
			roles.put(UserAuthorities.ROLE_DEALER.code(), UserAuthorities.ROLE_DEALER.name());
			roles.put(UserAuthorities.ROLE_CLIENT.code(), UserAuthorities.ROLE_CLIENT.name());
			model.addAttribute("roles", roles);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			try {
				model.addAttribute("dealers", dealerService.findAllByAdminIdAndStatusAndArchive(adminService.findByUsernameAndArchive(username, false).getAdminId(), true, false));
				model.addAttribute("adminId", adminService.findByUsernameAndArchive(username, false).getAdminId());
			} catch (SilException e) {
				logger.error(e.getMessage(), e);
			}
			model.addAttribute("users", users);
			return LOCATION + "create_user";
		}

		String em = "User not created";
		String sm = "User created successfully";
		users.setRegisterDate(new Date());

		Admin admin = null;
		Dealer dealer = null;
		Client client = null;

		if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
			admin = setAdminInofFromUserAndSaveBoth(users);
			if(admin == null) { 
				redirect.addFlashAttribute("em", em);
			}
			redirect.addFlashAttribute("sm", sm);
		}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
			dealer = setDealerInfoFromUserAndSaveBoth(users);
			if(dealer == null) { 
				redirect.addFlashAttribute("em", em);
			}
			redirect.addFlashAttribute("sm", sm);
		}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
			client = setClientInfoFromUserAndSaveBoth(users);
			if(client == null) { 
				redirect.addFlashAttribute("em", em);
			}
			redirect.addFlashAttribute("sm", sm);
		}

		return REDIRECT + REDIRECT_TO + "/create";
	}

	private Admin setAdminInofFromUserAndSaveBoth(Users users) {
		Admin admin = new Admin();
		admin.setAdminName(users.getName());
		admin.setUsername(users.getUsername());
		admin.setEmail(users.getEmail());
		admin.setPassword(users.getPassword());
		admin.setMobile(users.getMobile());
		admin.setStatus(users.isEnabled());
		admin.setArchive(users.isArchive());
		admin.setRegisterDate(users.getRegisterDate());
		admin.setAddress(users.getAddress());

		//save both
		try {
			usersService.save(users);
			adminService.save(admin);
			setAuthoritiesFromUsersAndSave(users);
			return admin;
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private Client setClientInfoFromUserAndSaveBoth(Users users) {
		Client client = new Client();
		client.setClientName(users.getName());
		client.setEmail(users.getEmail());
		client.setMobile(users.getMobile());
		client.setRegisterDate(users.getRegisterDate());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Date nextYear = calendar.getTime();
		client.setExpireDate(nextYear);
		client.setAddress(users.getAddress());
		client.setStatus(users.isEnabled());
		client.setDealerId(users.getDealerId());
		client.setUsername(users.getUsername());
		client.setPassword(users.getPassword());
		client.setArchive(users.isArchive());
		client.setSmsService(users.isSmsService());

		try {
			usersService.save(users);
			clientService.save(client);
			setAuthoritiesFromUsersAndSave(users);
			return client;
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}		

		return null;
	}

	private Dealer setDealerInfoFromUserAndSaveBoth(Users users) {
		Dealer dealer = new Dealer();
		dealer.setAdminId(users.getAdminId());
		dealer.setDealerName(users.getName());
		dealer.setEmail(users.getEmail());
		dealer.setUsername(users.getUsername());
		dealer.setPassword(users.getPassword());
		dealer.setAddress(users.getAddress());
		dealer.setMobile(users.getMobile());
		dealer.setRegisterDate(dealer.getRegisterDate());
		dealer.setStatus(users.isEnabled());
		dealer.setArchive(users.isArchive());
		dealer.setRegisterDate(users.getRegisterDate());

		try {
			usersService.save(users);
			dealerService.save(dealer);
			setAuthoritiesFromUsersAndSave(users);
			return dealer;
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private Authorities setAuthoritiesFromUsersAndSave(Users users) {
		Authorities authorities = new Authorities();
		authorities.setUsername(users.getUsername());
		authorities.setAuthority(users.getAuthority());
		authorities.setArchive(users.isArchive());
		try {
			authoritiesService.save(authorities);
			return authorities;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@GetMapping
	public String loadAllUser(Model model) {
		List<Users> allUsers = usersService.findAll();
		if(allUsers == null) {
			model.addAttribute("em", "No users found to display");
			return REDIRECT;
		}

		try {
			for(Users users : allUsers) {
				if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
					Admin admin = adminService.findByUsername(users.getUsername());
					users.setName(admin.getAdminName());
					users.setMobile(admin.getMobile());
					users.setRegisterDate(admin.getRegisterDate());
					users.setRole(UserAuthorities.ROLE_ADMIN.code());
				}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
					Dealer dealer = dealerService.findByUsername(users.getUsername());
					users.setName(dealer.getDealerName());
					users.setMobile(dealer.getMobile());
					users.setRegisterDate(dealer.getRegisterDate());
					users.setRole(UserAuthorities.ROLE_DEALER.code());
				}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
					Client client = clientService.findByUsername(users.getUsername());
					users.setName(client.getClientName());
					users.setMobile(client.getMobile());
					users.setRegisterDate(client.getRegisterDate());
					users.setRole(UserAuthorities.ROLE_CLIENT.code());
				}
			}
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		model.addAttribute("pageTitle", PAGE_TITLE);
		model.addAttribute("users", allUsers.stream().filter(u -> u.isArchive() == Boolean.FALSE).collect(Collectors.toList()));
		model.addAttribute("archivedUsers", allUsers.stream().filter(u -> u.isArchive() == Boolean.TRUE).collect(Collectors.toList()));
		return LOCATION + LOCATION_TO;
	}

	@GetMapping("/create")
	public String usersCreatePage(Model model) {
		model.addAttribute("pageTitle", "Create User");
		Map<String, String> roles = new HashMap<>();
		roles.put(UserAuthorities.ROLE_ADMIN.code(), UserAuthorities.ROLE_ADMIN.name());
		roles.put(UserAuthorities.ROLE_DEALER.code(), UserAuthorities.ROLE_DEALER.name());
		roles.put(UserAuthorities.ROLE_CLIENT.code(), UserAuthorities.ROLE_CLIENT.name());
		model.addAttribute("roles", roles);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		try {
			model.addAttribute("dealers", dealerService.findAllByAdminIdAndStatusAndArchive(adminService.findByUsernameAndArchive(username, false).getAdminId(), true, false));
			model.addAttribute("adminId", adminService.findByUsernameAndArchive(username, false).getAdminId());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		model.addAttribute("users", new Users());
		return LOCATION + "create_user";
	}

	@GetMapping("/changeStatus/{username}")
	public String changeUserStatus(@PathVariable("username") String username, RedirectAttributes redirect) {
		Users users = null;
		Admin admin = null;
		Dealer dealer = null;
		Client client = null;
		try {
			users = usersService.findByUsernameAndArchive(username, false);
			if(users == null) {
				redirect.addFlashAttribute("em", "User Not found");
				return REDIRECT + REDIRECT_TO;
			}

			users.setEnabled(!users.isEnabled());

			if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_ADMIN.name())) {
				admin = adminService.findByUsername(users.getUsername());
				admin.setStatus(users.isEnabled());
				adminService.save(admin);
			}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_DEALER.name())) {
				dealer = dealerService.findByUsername(users.getUsername());
				dealer.setStatus(users.isEnabled());
				dealerService.save(dealer);
			}else if(users.getAuthority().equalsIgnoreCase(UserAuthorities.ROLE_CLIENT.name())) {
				client = clientService.findByUsername(users.getUsername());
				client.setStatus(users.isEnabled());
				clientService.save(client);
			}

			usersService.save(users);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
			redirect.addFlashAttribute("em", "Status not cahnge");
			return REDIRECT + REDIRECT_TO;
		}

		redirect.addFlashAttribute("sm", "User status change successfully");
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/checkusername", method = RequestMethod.POST)
	public @ResponseBody ResponseMessage checkUsernameAvailability(Users users) {
		ResponseMessage responseMessage = new ResponseMessage();
		long count = 0;
		count = usersService.findAll().stream().filter(r -> r.getUsername().equals(users.getUsername())).count();
		if(count > 0) {
			responseMessage.setStatus(true);
		}else {
			responseMessage.setStatus(false);
		}
		return responseMessage;
	}

	@RequestMapping(value = "/checkemail", method = RequestMethod.POST)
	public @ResponseBody ResponseMessage checkEmailAvailability(Users users) {
		ResponseMessage responseMessage = new ResponseMessage();
		long count = 0;
		count = usersService.findAll().stream().filter(r -> r.getEmail().equals(users.getEmail())).count();
		if(count > 0) {
			responseMessage.setStatus(true);
		}else {
			responseMessage.setStatus(false);
		}
		return responseMessage;
	}

}
