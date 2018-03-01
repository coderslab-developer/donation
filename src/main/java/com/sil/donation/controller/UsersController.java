package com.sil.donation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.ResponseMessage;
import com.sil.donation.model.UserRole;
import com.sil.donation.service.AdminService;
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

	@GetMapping
	public String loadAllUser(Model model) {
		List<Users> allUsers = usersService.findAll();
		if(allUsers == null) {
			model.addAttribute("em", "No users found to display");
			return REDIRECT;
		}

		try {
			for(Users users : allUsers) {
				if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_ADMIN.name())) {
					Admin admin = adminService.findByUsername(users.getUsername());
					users.setName(admin.getAdminName());
					users.setMobile(admin.getMobile());
					users.setRegisterDate(admin.getRegisterDate());
					users.setRole("ADMIN");
				}else if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_DEALER.name())) {
					Dealer dealer = dealerService.findByUsername(users.getUsername());
					users.setName(dealer.getDealerName());
					users.setMobile(dealer.getMobile());
					users.setRegisterDate(dealer.getRegisterDate());
					users.setRole("DEALER");
				}else if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_CLIENT.name())) {
					Client client = clientService.findByUsername(users.getUsername());
					users.setName(client.getClientName());
					users.setMobile(client.getMobile());
					users.setRegisterDate(client.getRegisterDate());
					users.setRole("CLIENT");
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

			if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_ADMIN.name())) {
				admin = adminService.findByUsername(users.getUsername());
				admin.setStatus(users.isEnabled());
				adminService.save(admin);
			}else if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_DEALER.name())) {
				dealer = dealerService.findByUsername(users.getUsername());
				dealer.setStatus(users.isEnabled());
				dealerService.save(dealer);
			}else if(users.getAuthority().equalsIgnoreCase(UserRole.ROLE_CLIENT.name())) {
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
