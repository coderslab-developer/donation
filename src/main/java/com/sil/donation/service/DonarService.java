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
}
