/**
 * 
 */
package com.sil.donation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sil.donation.entity.SMSNotifier;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.SMSNotifierService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/smsNotifier")
public class SMSNotifierController {

	private static final Logger logger = LoggerFactory.getLogger(SMSNotifierController.class);

	@Autowired SMSNotifierService smsNotifierService;

	@GetMapping("/close/{username}")
	public String closeNotificationForTodya(@PathVariable("username") String username) {
		List<SMSNotifier> notices = null;
		try {
			notices = smsNotifierService.findAllByUsernameAndStatus(username, true);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(notices != null) {
			for(SMSNotifier notifier : notices) {
				notifier.setStatus(false);
				smsNotifierService.save(notifier);
			}
		}

		return "redirect:/";
	}
}
