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

	public boolean save(Donar donar) {
		Donar d = donarRepository.save(donar);
		if(d == null) {
			return false; 
		}
		return true;
	}

	public List<Donar> findAllByClientIdAndArchive(Integer clientId, boolean archive) throws SilException{
		return donarRepository.findAllByClientIdAndArchive(clientId, archive);
	}

	public Donar findByDonarId(Integer donarId, boolean archive) throws SilException {
		return donarRepository.findByDonarIdAndArchive(donarId, archive);
	}

	public List<Donar> findAllByClientId(Integer clientId) throws SilException{
		return donarRepository.findAllByClientId(clientId);
	}

	public List<Donar> findAllByClientIdAndSmsServiceAndStatusAndArchive(Integer clientId, boolean smsService, boolean status, boolean archive) throws SilException{
		return donarRepository.findAllByClientIdAndSmsServiceAndStatusAndArchive(clientId, smsService, status, archive);
	}

	public List<Donar> findAllByArchive(boolean archive) throws SilException {
		return donarRepository.findAllByArchive(archive);
	}
}
