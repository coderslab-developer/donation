/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SMSInfoTracker;
import com.sil.donation.repository.SMSInfoTrackerRepository;

/**
 * @author zubayer
 *
 */
@Service
public class SMSInfoTrackerService {

	@Autowired private SMSInfoTrackerRepository smsInfoTrackerRepository;

	public boolean saveSMSInforTrack(SMSInfoTracker smsInfoTracker) {
		SMSInfoTracker st = smsInfoTrackerRepository.save(smsInfoTracker);
		return st == null ? false : true;
	}
}
