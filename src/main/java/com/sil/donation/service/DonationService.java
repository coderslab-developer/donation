package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Donation;
import com.sil.donation.repository.DonationRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class DonationService {

	@Autowired DonationRepository donationRepository;
	
	public boolean save(Donation donation) {
		Donation d = donationRepository.save(donation);
		if(d == null) {
			return false;
		}
		return true;
	}
}
