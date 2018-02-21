/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSKeyGenerator;
import com.sil.donation.repository.SMSKeyGeneratorRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class SMSKeyGeneratorService {

	@Autowired
	private SMSKeyGeneratorRepository smsKeyGeneratorRepository;

	public SMSKeyGenerator save(SMSKeyGenerator smsKeyGenerator) {
		SMSKeyGenerator s = smsKeyGeneratorRepository.save(smsKeyGenerator);
		return s != null ? s : null;
	}
}
