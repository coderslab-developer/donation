package com.sil.donation.schedular;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class SMSSchedular {

	private static final Logger logger = LoggerFactory.getLogger(SMSSchedular.class);

	@Scheduled(cron = "0 0 8 * * *") // 8 o'clock of every day
	public void sendSMSSchedular() {
		logger.info("SMS service call on {}", new Date());
	}
}
