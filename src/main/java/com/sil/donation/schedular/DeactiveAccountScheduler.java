/**
 * 
 */
package com.sil.donation.schedular;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.UsersService;

/**
 * @author Zubayer Ahamaed
 *
 */
@Component
public class DeactiveAccountScheduler {

	private static final Logger logger = LoggerFactory.getLogger(DeactiveAccountScheduler.class);

	@Autowired private ClientService clientService;
	@Autowired private UsersService usersService;

	//deactive expired client account
	@Scheduled(cron = "0 0 5 * * *") 
	private void deactiveExpiredClientAccout() {
		List<Client> clients = new ArrayList<>();
		try {
			clients = clientService.findByStatusAndArchive(true, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		if(!clients.isEmpty()) {
			for(Client client : clients) {
				cal1.setTime(client.getExpireDate());
				cal2.setTime(new Date());
				if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
					deactive(client);
				}
			}
		}

		logger.info("Deactive expired client account at : {}", new Date());
	}

	private void deactive(Client client) {
		client.setStatus(false);
		try {
			Users users = usersService.findByUsernameAndArchive(client.getUsername(), false);
			users.setEnabled(false);
			clientService.save(client);
			usersService.save(users);
			logger.info("Expired client deactive at {} and client {}", new Date(), client);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		};
	}
}
