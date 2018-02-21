/**
 * 
 */
package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.SMSTransactionRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class SMSTransactionService {

	@Autowired
	SMSTransactionRepository smsTransactionRepository;

	public List<SMSTransaction> findByUsernameOrderByUsernameDesc(String username) throws SilException {
		return smsTransactionRepository.findByUsernameOrderByUsernameDesc(username);
	}

	public SMSTransaction save(SMSTransaction smsTransaction) {
		return smsTransactionRepository.save(smsTransaction);
	}
}
