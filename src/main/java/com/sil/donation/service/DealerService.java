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

	/**
	 * Save {@link Dealer}
	 * @param dealer
	 * @return boolean
	 * @throws SilException
	 */
	public boolean save(Dealer dealer) throws SilException {
		Dealer d = dealerRepository.save(dealer);
		return d != null ? true : false;
	}

	/**
	 * Find {@link Dealer} by dealerId and archive
	 * @param dealerId
	 * @param archive
	 * @return {@link Dealer}
	 * @throws SilException
	 */
	public Dealer findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException {
		return dealerRepository.findByDealerIdAndArchive(dealerId, archive);
	}

	/**
	 * Find {@link Dealer} by username and archive
	 * @param username
	 * @param archive
	 * @return {@link Dealer}
	 * @throws SilException
	 */
	public Dealer findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return dealerRepository.findByUsernameAndArchive(username, archive);
	}

	/**
	 * Find all {@link Dealer}
	 * @return List<{@link Dealer}>
	 */
	public List<Dealer> findAll() {
		return dealerRepository.findAll();
	}

	/**
	 * Find all {@link Dealer} by archive
	 * @param archive
	 * @return List<{@link Dealer}>
	 * @throws SilException
	 */
	public List<Dealer> findAllByArchive(boolean archive) throws SilException {
		return dealerRepository.findAllByArchive(archive);
	}

	/**
	 * Find all {@link Dealer} by status and archive
	 * @param status
	 * @param archive
	 * @return List<{@link Dealer}>
	 * @throws SilException
	 */
	public List<Dealer> findAllByStatusAndArchive(boolean status, boolean archive) throws SilException {
		return dealerRepository.findAllByStatusAndArchive(status, archive);
	}

	/**
	 * Find all {@link Dealer} by adminId, status and archive
	 * @param adminId
	 * @param status
	 * @param archive
	 * @return List<{@link Dealer}>
	 * @throws SilException
	 */
	public List<Dealer> findAllByAdminIdAndStatusAndArchive(Integer adminId, boolean status, boolean archive) throws SilException {
		return dealerRepository.findAllByAdminIdAndStatusAndArchive(adminId, status, archive);
	}

	/**
	 * Find {@link Dealer} by username
	 * @param username
	 * @return {@link Dealer}
	 * @throws SilException
	 */
	public Dealer findByUsername(String username) throws SilException{
		return dealerRepository.findByUsername(username);
	}
}
