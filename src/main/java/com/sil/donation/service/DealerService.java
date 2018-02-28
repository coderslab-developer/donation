package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.DealerRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class DealerService {

	@Autowired
	private DealerRepository dealerRepository;

	public boolean save(Dealer dealer) throws Exception {
		Dealer d = dealerRepository.save(dealer);
		return d != null ? true : false;
	}

	public Dealer findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException {
		return dealerRepository.findByDealerIdAndArchive(dealerId, archive);
	}

	public Dealer findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return dealerRepository.findByUsernameAndArchive(username, archive);
	}

	public List<Dealer> findAll() {
		return dealerRepository.findAll();
	}

	public List<Dealer> findAllByArchive(boolean archive) throws SilException {
		return dealerRepository.findAllByArchive(archive);
	}

	public List<Dealer> findAllByStatusAndArchive(boolean status, boolean archive) throws SilException {
		return dealerRepository.findAllByStatusAndArchive(status, archive);
	}

	public List<Dealer> findAllByAdminIdAndStatusAndArchive(Integer adminId, boolean status, boolean archive) throws SilException {
		return dealerRepository.findAllByAdminIdAndStatusAndArchive(adminId, status, archive);
	}
}
