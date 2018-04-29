/**
 * 
 */
package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSNotifier;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.SMSNotifierRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class SMSNotifierService {

	@Autowired private SMSNotifierRepository smsNotifierRepository;

	/**
	 * Save {@link SMSNotifier}
	 * @param smsNotifier
	 * @return boolean
	 */
	public boolean save(SMSNotifier smsNotifier) {
		SMSNotifier sn = smsNotifierRepository.save(smsNotifier);
		return sn != null ? true : false;
	}

	/**
	 * Find all {@link SMSNotifier} by username, status and ORDER BY id DESC
	 * @param username
	 * @param status
	 * @return List<{@link SMSNotifier}>
	 * @throws SilException
	 */
	public List<SMSNotifier> findByUsernameAndStatusOrderByIdDesc(String username, boolean status) throws SilException {
		return smsNotifierRepository.findByUsernameAndStatusOrderByIdDesc(username, status);
	}

	/**
	 * Find all {@link SMSNotifier} by username and status
	 * @param username
	 * @param status
	 * @return List<{@link SMSNotifier}>
	 * @throws SilException
	 */
	public List<SMSNotifier> findAllByUsernameAndStatus(String username, boolean status) throws SilException{
		return smsNotifierRepository.findAllByUsernameAndStatus(username, status);
	}
}
