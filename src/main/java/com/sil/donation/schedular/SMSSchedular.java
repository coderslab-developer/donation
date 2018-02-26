package com.sil.donation.schedular;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.SMSTransactionService;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class SMSSchedular {

	private static final Logger logger = LoggerFactory.getLogger(SMSSchedular.class);

	@Autowired
	private SMSTransactionService smsTransactionService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private DonarService donarService;

	// 8 o'clock of every day
	@Scheduled(cron = "0 0 20 * * *") 
	private void sendSMSSchedular()  {
		List<Client> clients = new ArrayList<>();
		List<Donar> donars = new ArrayList<>();

		try {
			clients = getAllClient().stream().filter(c -> Boolean.TRUE == checkClientHasAvailableSMS(c)).collect(Collectors.toList());
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}

		for(Client c : clients) {
			logger.info("{}", c);
		}

		if(!clients.isEmpty()) {
			for(Client client : clients) {
				List<Donar> list = getAllDonarWhichHaveSMSScheduleToday(client);
				for(Donar d : list) {
					donars.add(d);
				}
			}
		}

		if(!donars.isEmpty()) {
			for(Donar donar : donars) {
				logger.info("Send SMS to  {}", donar);
			}
		}else {
			logger.info("No Donar found for sms today");
		}

		logger.info("SMS service call on {}", new Date());
	}

	//get clients whose have active sms donar
	private List<Donar> getDonars(Client client) throws SilException{
		return donarService.findAllByClientIdAndSmsServiceAndStatusAndArchive(client.getClientId(), true, true, false);
	} 

	//get all current date sms donar
	private List<Donar> getAllDonarWhichHaveSMSScheduleToday(Client client){
		List<Donar> filteredDonars = new ArrayList<>();
		try {
			List<Donar> donars = getDonars(client);
			if(donars.isEmpty()) {
				return donars;
			}
			for(Donar donar : donars) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(donar.getSmsDate());
				cal2.setTime(new Date());
				if(cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)) {
					filteredDonars.add(donar);
				}
			}
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
		return filteredDonars;
	}

	//get all client whose have sms service and active and not archive
	private List<Client> getAllClient() throws SilException{
		List<Client> clients = null;
		clients = clientService.findAllBySmsServiceAndStatusAndArchive(true, true, false);
		return clients;
	}

	//check the client has available sms to send autometically
	private boolean checkClientHasAvailableSMS(Client client) {
		int smsAmount = 0;
		int activeSmsServiceAvailableDonar = 0;

		SMSTransaction smsTransaction = null;
		List<SMSTransaction> smsTransactions = new ArrayList<>();
		try {
			smsTransactions = smsTransactionService.findByUsernameOrderByIdDesc(client.getUsername());
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
		if(!smsTransactions.isEmpty()) {
			smsTransaction = smsTransactions.get(0);
		}

		if(smsTransaction == null) {
			//no sms available
			return false;
		}

		smsAmount = smsTransaction.getAvailableSMS();
		try {
			activeSmsServiceAvailableDonar = getDonars(client).size();
		} catch (SilException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}

		return smsAmount < activeSmsServiceAvailableDonar ? false : true; 
	}
	
}
