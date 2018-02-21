package com.sil.donation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sil.donation.entity.Users;
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

	@Autowired private UsersService usersService; 

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
