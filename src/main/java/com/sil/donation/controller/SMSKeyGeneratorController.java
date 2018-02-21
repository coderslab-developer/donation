/**
 * 
 */
package com.sil.donation.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sil.donation.entity.SMSKeyGenerator;
import com.sil.donation.service.SMSKeyGeneratorService;
import com.sil.donation.util.RandomNumberGenerator;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/smsKey")
public class SMSKeyGeneratorController {

	private static final Logger logger = LoggerFactory.getLogger(SMSKeyGeneratorController.class);

	@Autowired
	private SMSKeyGeneratorService smsKeyGeneratorService;

	@GetMapping("/generate")
	public @ResponseBody Map<String, String> generateSMSKey(Model model) {
		Map<String, String> response = new HashMap<>();

		RandomNumberGenerator rng = new RandomNumberGenerator();

		SMSKeyGenerator smsKeyGenerator = new SMSKeyGenerator();
		smsKeyGenerator.setSmsKey(rng.generateKey());
		smsKeyGenerator.setStatus(true);

		SMSKeyGenerator s = smsKeyGeneratorService.save(smsKeyGenerator);

		if (s == null) {
			response.put("smsKey", null);
			response.put("status", "failed");
			return response;
		}

		if (logger.isDebugEnabled())
			logger.debug("SMSKeyGenerator Details : {}", s);
		
		response.put("smsKey", s.getSmsKey());
		response.put("status", "success");
		return response;
	}

}
