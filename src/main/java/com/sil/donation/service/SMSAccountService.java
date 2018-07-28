/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSAccount;
import com.sil.donation.repository.SMSAccountRepository;

/**
 * @author zubayer
 *
 */
@Service
public class SMSAccountService {

	@Autowired private SMSAccountRepository smsAccountRepository;

	public boolean saveSMSAccount(SMSAccount smsAccount) {
		SMSAccount sa = smsAccountRepository.save(smsAccount);
		return sa == null ? false : true;
	}

	public SMSAccount findSMSAccount(String secretName, boolean active, boolean archive) {
		return smsAccountRepository.findBySecretNameAndActiveAndArchive(secretName, active, archive);
	}
}
