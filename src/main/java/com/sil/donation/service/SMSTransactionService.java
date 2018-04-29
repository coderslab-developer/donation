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

	/**
	 * Find all {@link SMSTransaction}
	 * @return List<{@link SMSTransaction}>
	 */
	public List<SMSTransaction> findAll() {
		return smsTransactionRepository.findAll();
	}

	/**
	 * Find all {@link SMSTransaction} by username ORDER BY id DESC
	 * @param username
	 * @return List<{@link SMSTransaction}>
	 * @throws SilException
	 */
	public List<SMSTransaction> findByUsernameOrderByIdDesc(String username) throws SilException {
		return smsTransactionRepository.findByUsernameOrderByIdDesc(username);
	}

	/**
	 * Save {@link SMSTransaction}
	 * @param smsTransaction
	 * @return {@link SMSTransaction}
	 */
	public SMSTransaction save(SMSTransaction smsTransaction) {
		return smsTransactionRepository.save(smsTransaction);
	}
}
