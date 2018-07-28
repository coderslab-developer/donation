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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.SMSKeyGenerator;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.SMSActionPerform;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.service.AdminService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.SMSKeyGeneratorService;
import com.sil.donation.service.SMSTransactionService;
import com.sil.donation.service.UsersService;
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
	@Autowired private SMSKeyGeneratorService smsKeyGeneratorService;
	@Autowired private UsersService usersService;
	@Autowired private AdminService adminService;
	@Autowired private DealerService dealerService;
	@Autowired private ClientService clientService;

	@RequestMapping(value = "/updateSMS", method = RequestMethod.POST)
	public String updateAdminSMS(String username, int smsAmount, RedirectAttributes redirect) {
		if(smsAmount <= 0) {
			redirect.addFlashAttribute("em", "Please enter sms quantity greatter then 0");
			return "redirect:/smsBucket/" + username;
		}
		
		SMSTransaction smsTransaction = new SMSTransaction();
		smsTransaction.setUsername(username);
		smsTransaction.setActionPerform(SMSActionPerform.BUY.name());
		smsTransaction.setActionDate(new Date());

		SMSKeyGenerator smsKeyGenerator = new SMSKeyGenerator();
		smsKeyGenerator.setSmsKey(new RandomNumberGenerator().generateKey());
		smsKeyGenerator.setStatus(true);
		smsKeyGeneratorService.save(smsKeyGenerator);
		smsTransaction.setSmsKey(smsKeyGenerator.getSmsKey());
		smsTransaction.setBuyFrom("THIRDPARTY");
		smsTransaction.setBuyQty(smsAmount);
		smsTransaction.setSellQty(0);
		//smsTransaction.setSellTo(findUserFullName(username));
		smsTransaction.setBuyerUsername(username);
		//smsTransaction.setSellerUsername("");

		SMSTransaction st = null;
		try {
			List<SMSTransaction> smsTransactions = smsTransactionService.findByUsernameOrderByIdDesc(username);
			if(!smsTransactions.isEmpty()) {
				st = smsTransactions.get(0);
			}
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		if(st == null) {
			smsTransaction.setAvailableSMS(smsAmount);
			smsTransaction.setUsedSMS(0);
		}else {
			int newSMSAmount = smsAmount + st.getAvailableSMS();
			smsTransaction.setAvailableSMS(newSMSAmount);
			smsTransaction.setUsedSMS(st.getUsedSMS());
		}

		SMSTransaction returnedSMSTransaction = smsTransactionService.save(smsTransaction);
		if(returnedSMSTransaction == null) {
			redirect.addFlashAttribute("em", "SMS Quantity not update");
			return "redirect:/smsBucket/" + username;
		}

		try {
			SMSKeyGenerator skg = smsKeyGeneratorService.findBySmsKeyAndStatus(smsKeyGenerator.getSmsKey(), true);
			skg.setStatus(false);
			smsKeyGeneratorService.save(smsKeyGenerator);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		redirect.addFlashAttribute("sm", "SMS Quantity update successfully");
		return "redirect:/smsBucket/" + username;
	}

	@RequestMapping(value = "/sellSMS", method = RequestMethod.POST)
	public String smsSellToDealer(String username, String smsKey, String smsAmount, String sellerUsername, RedirectAttributes redirect) {
		if(username.equalsIgnoreCase("none") || smsKey == null || smsAmount == null) {
			redirect.addFlashAttribute("em", "Please fillup the form correctly");
			return "redirect:/smsBucket/" + sellerUsername;
		}
		logger.info(username);

		List<SMSTransaction> sellers = null;
		SMSTransaction seller = null;
		try {
			sellers = smsTransactionService.findByUsernameOrderByIdDesc(sellerUsername);
			if(!sellers.isEmpty()) {
				seller = sellers.get(0);
				//check seller have available sms to sell
				if(seller.getAvailableSMS() <= Integer.valueOf(smsAmount)) {
					redirect.addFlashAttribute("em", "You have no enough sms to sell");
					return "redirect:/smsBucket/" + sellerUsername;
				}
			}else {
				redirect.addFlashAttribute("em", "You have no enough sms to sell");
				return "redirect:/smsBucket/" + sellerUsername;
			}
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		SMSTransaction smsTransaction = null;
		try {
			//find previous transaction if available
			List<SMSTransaction> smsTransactions = smsTransactionService.findByUsernameOrderByIdDesc(username);
			if(!smsTransactions.isEmpty()) {
				smsTransaction = smsTransactions.get(0);
			}
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		if(smsTransaction != null) {
			//if have old transaction
			smsTransaction.setAvailableSMS(Integer.valueOf(smsAmount) + smsTransaction.getAvailableSMS());
			smsTransaction.setUsedSMS(smsTransaction.getUsedSMS());
		}else {
			//if new transaction
			smsTransaction = new SMSTransaction();
			smsTransaction.setAvailableSMS(Integer.valueOf(smsAmount));
			smsTransaction.setUsedSMS(0);
		}
		SMSTransaction st = new SMSTransaction();
		st.setAvailableSMS(smsTransaction.getAvailableSMS());
		st.setUsedSMS(smsTransaction.getUsedSMS());
		st.setUsername(username);
		st.setActionPerform(SMSActionPerform.BUY.name());
		st.setActionDate(new Date());
		st.setSmsKey(smsKey);
		st.setBuyFrom(findUserFullName(sellerUsername)); //todo
		st.setBuyQty(Integer.parseInt(smsAmount));
		st.setSellQty(0);
		//st.setSellTo(findUserFullName(username));
		st.setBuyerUsername(username);
		st.setSellerUsername(sellerUsername);

		try {
			SMSKeyGenerator smsKeyGenerator = smsKeyGeneratorService.findBySmsKeyAndStatus(smsKey, true);
			smsKeyGenerator.setStatus(false);
			smsKeyGeneratorService.save(smsKeyGenerator);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}

		SMSTransaction returnedSmsTransaction = smsTransactionService.save(st);
		if(returnedSmsTransaction == null) {
			redirect.addFlashAttribute("em", "Transaction not successfull");
			return "redirect:/smsBucket/" + sellerUsername;
		}

		//change seller sms bucket balance and make new transaction
		if(seller != null) {
			SMSTransaction s = new SMSTransaction();
			s.setSmsKey(smsKey);
			s.setAvailableSMS(seller.getAvailableSMS() - Integer.valueOf(smsAmount));
			s.setUsedSMS(seller.getUsedSMS() + Integer.valueOf(smsAmount));
			s.setActionPerform(SMSActionPerform.SELL.name());
			s.setActionDate(new Date());
			s.setUsername(sellerUsername);
			//s.setBuyFrom(findUserFullName(sellerUsername)); //todo
			s.setBuyQty(0);
			s.setSellQty(Integer.parseInt(smsAmount));
			s.setSellTo(findUserFullName(username)); //todo
			s.setBuyerUsername(username);
			s.setSellerUsername(sellerUsername);
			smsTransactionService.save(s);
		}

		redirect.addFlashAttribute("sm", "Transaction successfull");
		return "redirect:/smsBucket/" + sellerUsername;
	}

	private String findUserFullName(String username) {
		String fullName = "";
		Users users;
		try {
			users = usersService.findByUsernameAndArchive(username, false);
			String role = users.getAuthority();
			if(role.equals(UserAuthorities.ROLE_ADMIN.name())) {
				Admin admin = adminService.findByUsername(username);
				return admin.getAdminName();
			} else if (role.equals(UserAuthorities.ROLE_DEALER.name())) {
				Dealer dealer = dealerService.findByUsername(username);
				return dealer.getDealerName();
			} else if (role.equals(UserAuthorities.ROLE_CLIENT.name())) {
				Client client = clientService.findByUsername(username);
				return client.getClientName();
			}
		} catch (SilException e) {
			logger.error("Error is - Message : {}, Full error : {}", e.getMessage(), e);
		}
		return fullName;
	}
}
