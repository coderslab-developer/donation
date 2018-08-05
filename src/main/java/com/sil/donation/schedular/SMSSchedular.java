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

import com.onnorokom.sms.v1.ArrayOfWsSms;
import com.onnorokom.sms.v1.SendSmsSoap;
import com.onnorokom.sms.v1.WsSms;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SMSAccount;
import com.sil.donation.entity.SMSInfoTracker;
import com.sil.donation.entity.SMSNotifier;
import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.SMSAccountService;
import com.sil.donation.service.SMSInfoTrackerService;
import com.sil.donation.service.SMSNotifierService;
import com.sil.donation.service.SMSTransactionService;
import com.sil.donation.service.OnnorkomObjectBuilder;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class SMSSchedular {

	private static final Logger logger = LoggerFactory.getLogger(SMSSchedular.class);
	private static final String SMS_ACCOUNT_SECRET_NAME = "ZUBAYER";
	String endPoint = "https://api2.onnorokomsms.com/sendsms.asmx";
	String apiKey = null;

	@Autowired private SMSTransactionService smsTransactionService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private SMSNotifierService smsNotifierService;
	@Autowired private SMSInfoTrackerService smsInfoTrackerService;
	@Autowired private SMSAccountService smsAccountService;

	// 8 o'clock of every day
	@Scheduled(cron = "0 0 20 * * *")
	//@Scheduled(fixedDelay = 3000)
	private void sendSMSSchedular() {
		getSMSApiKeyAndBaseUrl();
		if(endPoint == null || apiKey == null) return;
		if(endPoint.isEmpty() || apiKey.isEmpty()) return;

		//GET CLIENTS WHOSE HAS SMS CREDIT AND HAS SMS ACTIVE SERVICE
		List<Client> clients = new ArrayList<>();
		List<Donar> donars = new ArrayList<>();
		try {
			clients = getAllClient().stream().filter(c -> Boolean.TRUE == checkClientHasAvailableSMS(c)).collect(Collectors.toList());
			for(Client c : clients) logger.info("SMS available Client : {}", c.getClientName());
		} catch (SilException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}

		if(!clients.isEmpty()) {
			for(Client client : clients) {
				List<Donar> list = getAllDonarWhichHaveSMSScheduleToday(client);
				for(Donar d : list) {
					if(client.getAutoMessage() != null && !client.getAutoMessage().isEmpty()) {
						d.setAutoMessage(client.getAutoMessage());
						d.setMaskName(client.getMaskName());
						d.setCampaignName(client.getCampaignName());
						donars.add(d);
					}
				}
			}
		}

		if(!donars.isEmpty()) {
			List<String> numbers = new ArrayList<>();
			String maskName = donars.stream().findFirst().orElseGet(null).getMaskName();
			String campaignName = donars.stream().findFirst().orElseGet(null).getCampaignName();
			for(Donar donar : donars) {
				logger.info("Scheduled donar for Send SMS : {}", donar);
				if(donar.getMobile() != null || !donar.getMobile().isEmpty()) {
					numbers.add(donar.getMobile());
				}
			}

			//donar limitation 500 per day
			if(numbers.size() > 500) {
				numbers = numbers.stream().limit(500).collect(Collectors.toList());
			}

			SendSmsSoap port = OnnorkomObjectBuilder.getSMSPort(endPoint);
			List<WsSms> wsSmses = new ArrayList<>();
			for(Donar donar : donars) {
				WsSms wsSms = new WsSms();
				wsSms.setMobileNumber(donar.getMobile());
				wsSms.setSmsText(donar.getAutoMessage());
				wsSms.setType("TEXT");
				wsSmses.add(wsSms);
			}
			ArrayOfWsSms arrayOfWsSms = new ArrayOfWsSms();
			arrayOfWsSms.setWsSms(wsSmses);
			String value = port.listSms(apiKey, arrayOfWsSms, maskName == null ? "" : maskName, campaignName == null ? "" : campaignName);
			String[] fullSMS = value.split("/");
			for(int i = 0; i < fullSMS.length; i++) {
				logger.info(fullSMS[i]);
				String[] values = fullSMS[i].split("\\|\\|");
				String code = "";
				String number = "";
				String reference = "";
				for(int j = 0; j < values.length; j++) {
					switch (j) {
					case 0 :
						code = values[0];
						break;
					case 1 :
						number = values[1];
						break;
					case 2 :
						reference = values[2];
						break;
					}
				}

				SMSInfoTracker smsInfoTracker = new SMSInfoTracker();
				smsInfoTracker.setCode(code);
				smsInfoTracker.setMobileNumber(number);
				smsInfoTracker.setReferenceCode(reference);
				smsInfoTracker.setSmsDateTime(new Date());
				smsInfoTrackerService.saveSMSInforTrack(smsInfoTracker);
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

	//get all one day before scheduled date sms donar
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
				cal1.add(Calendar.DATE, -1);
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

		if(smsAmount < activeSmsServiceAvailableDonar) {
			SMSNotifier smsNotifier = new SMSNotifier();
			smsNotifier.setUsername(client.getUsername());
			smsNotifier.setAvailableSMS(smsAmount);
			smsNotifier.setAvailableSMSClient(activeSmsServiceAvailableDonar);
			smsNotifier.setStatus(true);
			smsNotifier.setMessage("You have not enough SMS in your bucket to send Automated SMS to SMS Service Activated " + activeSmsServiceAvailableDonar + " donar");
			smsNotifierService.save(smsNotifier);

			logger.info("Create Notice for {} at {}", client.getClientName(), new Date());
			return false;
		}else {
			List<SMSNotifier> notices = new ArrayList<>();
			try {
				notices = smsNotifierService.findAllByUsernameAndStatus(client.getUsername(), true);
			} catch (SilException e) {
				logger.error(e.getMessage(), e);
			}

			if(!notices.isEmpty()) {
				for(SMSNotifier notifier : notices) {
					notifier.setStatus(false);
					smsNotifierService.save(notifier);
				}
			}

			logger.info("Deactive {} sms notice for {} at {}", notices.size(), client.getClientName(), new Date());
			return true;
		}
	}

	//GET SMS API KEY AND ACCESS URL
	public void getSMSApiKeyAndBaseUrl() {
		SMSAccount smsAccount = smsAccountService.findSMSAccount(SMS_ACCOUNT_SECRET_NAME, true, false);
		logger.info("SMS Account : {}", smsAccount);
		if(smsAccount != null) {
			if(smsAccount.getBaseUrl() != null && !smsAccount.getBaseUrl().isEmpty()) this.endPoint = smsAccount.getBaseUrl();
			if(smsAccount.getApiKey() != null && !smsAccount.getApiKey().isEmpty()) this.apiKey = smsAccount.getApiKey();
		}
		logger.info("SMS Credentials == Apikey : {} ; Access Url : {}", apiKey, endPoint);
	}
}
