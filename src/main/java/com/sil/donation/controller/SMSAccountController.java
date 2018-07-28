package com.sil.donation.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.SMSAccount;
import com.sil.donation.service.SMSAccountService;

/**
 * 
 * @author zubayer
 *
 */
@Controller
@RequestMapping("/smsaccount")
public class SMSAccountController {

	private static final Logger logger = LoggerFactory.getLogger(SMSAccountController.class);
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "smsaccount";
	private static final String LOCATION_TO = "sms-account";
	private static final String LOCATION = "views/sms-account/";
	private static final String SMS_ACCOUNT_SECRET_NAME = "ZUBAYER";

	@Autowired
	private SMSAccountService smsAccountService;

	@RequestMapping
	public String loadSMSAccountPage(Model model) {
		SMSAccount smsAccount = smsAccountService.findSMSAccount(SMS_ACCOUNT_SECRET_NAME, true, false);
		model.addAttribute("smsAccount", smsAccount == null ? new SMSAccount() : smsAccount);
		return LOCATION + LOCATION_TO;
	}

	@PostMapping("/save")
	public String saveSMSAccountInfo(SMSAccount smsAccount, RedirectAttributes redirect) {
		logger.info("SMS Account data: {}", smsAccount);
		smsAccount.setCreateDate(new Date());
		smsAccount.setActive(true);
		smsAccount.setArchive(false);
		smsAccount.setSecretName(SMS_ACCOUNT_SECRET_NAME);

		boolean stat = smsAccountService.saveSMSAccount(smsAccount);
		if(Boolean.TRUE.equals(stat)) {
			redirect.addFlashAttribute("sm", "Account info saved successfully");
		}else {
			redirect.addFlashAttribute("em", "Account info not saved");
		}

		return REDIRECT + REDIRECT_TO;
	}
}
