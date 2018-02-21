/**
 * 
 */
package com.sil.donation.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.SMSActionPerform;
import com.sil.donation.service.SMSTransactionService;
import com.sil.donation.util.RandomNumberGenerator;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/smsTransaction")
public class SMSTransactionController {

	private static final Logger logger = LoggerFactory.getLogger(SMSKeyGeneratorController.class);

	@Autowired private SMSTransactionService smsTransactionService;

	@PostMapping("/updateSMS")
	public String updateAdminSMS(String username, String smsAmount, RedirectAttributes redirect) {
		SMSTransaction smsTransaction = new SMSTransaction();
		smsTransaction.setUsername(username);
		smsTransaction.setActionPerform(SMSActionPerform.BUY.name());
		smsTransaction.setActionDate(new Date());
		smsTransaction.setSmsKey(new RandomNumberGenerator().generateKey());

		SMSTransaction st = null;
		try {
			List<SMSTransaction> smsTransactions =  smsTransactionService.findByUsernameOrderByUsernameDesc(username);
			if(!smsTransactions.isEmpty()) {
				st = smsTransactions.get(0);
			}
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		if(st == null) {
			smsTransaction.setAvailableSMS(Integer.valueOf(smsAmount));
			smsTransaction.setUsedSMS(0);
		}else {
			int newSMSAmount = Integer.valueOf(smsAmount) + st.getAvailableSMS();
			smsTransaction.setAvailableSMS(newSMSAmount);
			smsTransaction.setUsedSMS(st.getUsedSMS());
		}

		SMSTransaction returnedSMSTransaction = smsTransactionService.save(smsTransaction);
		if(returnedSMSTransaction == null) {
			redirect.addFlashAttribute("em", "SMS Quantity not update");
			return "redirect:/smsBucket/" + username;
		}

		redirect.addFlashAttribute("sm", "SMS Quantity update successfully");
		return "redirect:/smsBucket/" + username;
	}

	@PostMapping("/dealer")
	public String smsSellToDealer(String username, String smsKey, String smsAmount, RedirectAttributes redirect) {
		if(username == null || smsKey == null || smsAmount == null) {
			redirect.addFlashAttribute("em", "Please fillup the form correctly");
			return "redirect:/smsBucket/username";
		}
		logger.info("Username : {}, SMS Key : {}, SMS Amount : {}", username , smsKey , smsAmount);

		return "redirect:/smsBucket/" + username;
	}
	
}
