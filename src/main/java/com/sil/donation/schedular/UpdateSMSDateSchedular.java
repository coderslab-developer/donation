package com.sil.donation.schedular;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.DonarService;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class UpdateSMSDateSchedular {

	private static final Logger logger = LoggerFactory.getLogger(UpdateSMSDateSchedular.class);

	@Autowired
	private DonarService donarService;
	@Autowired
	private CategoryService categoryService;

	//everyday 10 o'clock
	@Scheduled(cron = "0 0 22 * * *") 
	private void updateDonarsSMSDate() {
		List<Donar> donars = new ArrayList<>();
		try {
			donars = donarService.findAllByArchive(false);
		} catch (SilException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}

		if (!donars.isEmpty()) {
			for (Donar donar : donars) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(donar.getSmsDate());
				cal2.setTime(new Date());
				if (cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)) {
					updateSMSDate(donar);
				}
			}
		}

		logger.info("Update Donar SMS Date at : {}", new Date());
	}

	private void updateSMSDate(Donar donar) {
		try {
			int days = categoryService.findByCategoryIdAndArchive(donar.getCategoryId(), false).getDays();
			donar.setSmsDate(DateUtils.addDays(donar.getSmsDate(), days));
			donarService.save(donar);
		} catch (SilException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
	}
}
