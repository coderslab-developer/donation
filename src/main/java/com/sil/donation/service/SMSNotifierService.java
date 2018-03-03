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

	public boolean save(SMSNotifier smsNotifier) {
		SMSNotifier sn = smsNotifierRepository.save(smsNotifier);
		return sn != null ? true : false;
	}

	public List<SMSNotifier> findByUsernameAndStatusOrderByIdDesc(String username, boolean status) throws SilException {
		return smsNotifierRepository.findByUsernameAndStatusOrderByIdDesc(username, status);
	}

	public List<SMSNotifier> findAllByUsernameAndStatus(String username, boolean status) throws SilException{
		return smsNotifierRepository.findAllByUsernameAndStatus(username, status);
	}
}
