/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSKeyGenerator;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.SMSKeyGeneratorRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class SMSKeyGeneratorService {

	@Autowired
	private SMSKeyGeneratorRepository smsKeyGeneratorRepository;

	/**
	 * Save {@link SMSKeyGenerator}
	 * @param smsKeyGenerator
	 * @return {@link SMSKeyGenerator}
	 */
	public SMSKeyGenerator save(SMSKeyGenerator smsKeyGenerator) {
		SMSKeyGenerator s = smsKeyGeneratorRepository.save(smsKeyGenerator);
		return s != null ? s : null;
	}

	/**
	 * Find {@link SMSKeyGenerator} by smsKey and status
	 * @param smsKey
	 * @param status
	 * @return {@link SMSKeyGenerator}
	 * @throws SilException
	 */
	public SMSKeyGenerator findBySmsKeyAndStatus(String smsKey, boolean status) throws SilException {
		return smsKeyGeneratorRepository.findBySmsKeyAndStatus(smsKey, status);
	}
}
