package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.DonarRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class DonarService {

	@Autowired private DonarRepository donarRepository;

	/**
	 * Save {@link Donar}
	 * @param donar
	 * @return boolean
	 */
	public boolean save(Donar donar) {
		Donar d = donarRepository.save(donar);
		return d == null ? false : true;
	}

	/**
	 * Find all {@link Donar} by clientId and archive
	 * @param clientId
	 * @param archive
	 * @return List<{@link Donar}>
	 * @throws SilException
	 */
	public List<Donar> findAllByClientIdAndArchive(Integer clientId, boolean archive) throws SilException{
		return donarRepository.findAllByClientIdAndArchive(clientId, archive);
	}

	/**
	 * Find {@link Donar} by donarId
	 * @param donarId
	 * @param archive
	 * @return {@link Donar}
	 * @throws SilException
	 */
	public Donar findByDonarId(Integer donarId, boolean archive) throws SilException {
		return donarRepository.findByDonarIdAndArchive(donarId, archive);
	}

	/**
	 * Find all {@link Donar} by clientId
	 * @param clientId
	 * @return List<{@link Donar}>
	 * @throws SilException
	 */
	public List<Donar> findAllByClientId(Integer clientId) throws SilException{
		return donarRepository.findAllByClientId(clientId);
	}

	/**
	 * Find all {@link Donar} by clientId, smsService, status and archive
	 * @param clientId
	 * @param smsService
	 * @param status
	 * @param archive
	 * @return List<{@link Donar}>
	 * @throws SilException
	 */
	public List<Donar> findAllByClientIdAndSmsServiceAndStatusAndArchive(Integer clientId, boolean smsService, boolean status, boolean archive) throws SilException{
		return donarRepository.findAllByClientIdAndSmsServiceAndStatusAndArchive(clientId, smsService, status, archive);
	}

	/**
	 * Find all {@link Donar} by archive
	 * @param archive
	 * @return List<{@link Donar}>
	 * @throws SilException
	 */
	public List<Donar> findAllByArchive(boolean archive) throws SilException {
		return donarRepository.findAllByArchive(archive);
	}

	/**
	 * Find all {@link Donar} by clientId, status and archive
	 * @param clientId
	 * @param status
	 * @param archive
	 * @return List<{@link Donar}>
	 * @throws SilException
	 */
	public List<Donar> findAllByClientIdAndStatusAndArchive(Integer clientId, boolean status, boolean archive) throws SilException {
		return donarRepository.findAllByClientIdAndStatusAndArchive(clientId, status, archive);
	}
}
