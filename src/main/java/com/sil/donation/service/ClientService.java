package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Client;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.ClientRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	/**
	 * Save {@link Client}
	 * @param client
	 * @return boolean
	 * @throws SilException
	 */
	public boolean save(Client client) throws SilException{
		Client c = clientRepository.save(client);
		return c != null ? true : false;
	}

	/**
	 * Find All {@link Client} by dealerId and archive
	 * @param dealerId
	 * @param archive
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException {
		return clientRepository.findByDealerIdAndArchive(dealerId, archive);
	}

	/**
	 * Find all {@link Client}
	 * @return List<{@link Client}>
	 */
	public List<Client> findAll() {
		return clientRepository.findAll();
	}

	/**
	 * Find all {@link Client} by archive
	 * @param archive
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findAllByArchive(boolean archive) throws SilException {
		return clientRepository.findAllByArchive(archive);
	}

	/**
	 * Find all {@link Client} by dealerId
	 * @param dealerId
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findAllByDealerId(Integer dealerId) throws SilException {
		return clientRepository.findAllByDealerId(dealerId);
	}

	/**
	 * Find all {@link Client} by dealerId, status and archive
	 * @param dealerId
	 * @param status
	 * @param archive
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findByDealerIdAndStatusAndArchive(Integer dealerId, boolean status, boolean archive) throws SilException {
		return clientRepository.findByDealerIdAndStatusAndArchive(dealerId, status, archive);
	}

	/**
	 * Find all {@link Client} by status and archive
	 * @param status
	 * @param archive
	 * @return
	 * @throws SilException
	 */
	public List<Client> findByStatusAndArchive(boolean status, boolean archive) throws SilException {
		return clientRepository.findByStatusAndArchive(status, archive);
	}

	/**
	 * Find {@link Client} by clientId and archive
	 * @param clientId
	 * @param archive
	 * @return {@link Client}
	 * @throws SilException
	 */
	public Client findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException {
		return clientRepository.findByClientIdAndArchive(clientId, archive);
	}

	/**
	 * Find {@link Client} by userName and archive
	 * @param username
	 * @param archive
	 * @return {@link Client}
	 * @throws SilException
	 */
	public Client findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return clientRepository.findByUsernameAndArchive(username, archive);
	}

	/**
	 * Find all {@link Client} by dealerId, status, smsService and archive
	 * @param dealerId
	 * @param status
	 * @param smsService
	 * @param archive
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findAllByDealerIdAndStatusAndSmsServiceAndArchive(Integer dealerId, boolean status, boolean smsService, boolean archive) throws SilException{
		return clientRepository.findAllByDealerIdAndStatusAndSmsServiceAndArchive(dealerId, status, smsService, archive);
	}

	/**
	 * Find all {@link Client} by smsService, status and archive
	 * @param smsService
	 * @param status
	 * @param archive
	 * @return List<{@link Client}>
	 * @throws SilException
	 */
	public List<Client> findAllBySmsServiceAndStatusAndArchive(boolean smsService, boolean status, boolean archive) throws SilException{
		return clientRepository.findAllBySmsServiceAndStatusAndArchive(smsService, status, archive);
	}

	/**
	 * Find {@link Client} by userName
	 * @param username
	 * @return {@link Client}
	 * @throws SilException
	 */
	public Client findByUsername(String username) throws SilException {
		return clientRepository.findByUsername(username);
	}
}
