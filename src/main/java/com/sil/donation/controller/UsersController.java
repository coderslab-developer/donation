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

import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.ResponseMessage;
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

	@GetMapping
	public String loadAllUser(Model model) {
		List<Users> allUsers = usersService.findAll();

		model.addAttribute("pageTitle", PAGE_TITLE);
		model.addAttribute("users", allUsers.stream().filter(u -> u.isArchive() == Boolean.FALSE).collect(Collectors.toList()));
		model.addAttribute("archivedUsers", allUsers.stream().filter(u -> u.isArchive() == Boolean.TRUE).collect(Collectors.toList()));
		return LOCATION + LOCATION_TO;
	}

	@GetMapping("/changeStatus/{username}")
	public String changeUserStatus(@PathVariable("userId") String username, RedirectAttributes redirect) {
		Users users = null;
		try {
			users = usersService.findByUsernameAndArchive(username, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(users == null) {
			redirect.addFlashAttribute("em", "User Not found");
			return REDIRECT + REDIRECT_TO;
		}

		users.setEnabled(!users.isEnabled());
		boolean saveStatus = false;
		try {
			saveStatus = usersService.save(users);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(Boolean.FALSE == saveStatus) {
			redirect.addFlashAttribute("em", "User status not change");
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
