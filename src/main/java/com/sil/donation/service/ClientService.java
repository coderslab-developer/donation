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

	public boolean save(Client client) throws SilException{
		Client c = clientRepository.save(client);
		if (c != null) {
			return true;
		}
		return false;
	}

	public List<Client> findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException {
		return clientRepository.findByDealerIdAndArchive(dealerId, archive);
	}

	public List<Client> findAll() {
		return clientRepository.findAll();
	}
	
	public List<Client> findAllByArchive(boolean archive) throws SilException {
		return clientRepository.findAllByArchive(archive);
	}

	public List<Client> findByDealerIdAndStatusAndArchive(Integer dealerId, boolean status, boolean archive) throws SilException {
		return clientRepository.findByDealerIdAndStatusAndArchive(dealerId, status, archive);
	}

	public List<Client> findByStatusAndArchive(boolean status, boolean archive) throws SilException {
		return clientRepository.findByStatusAndArchive(status, archive);
	}

	public Client findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException {
		return clientRepository.findByClientIdAndArchive(clientId, archive);
	}

	public Client findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return clientRepository.findByUsernameAndArchive(username, archive);
	}
}
