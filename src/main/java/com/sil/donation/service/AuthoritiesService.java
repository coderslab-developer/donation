package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserRole;
import com.sil.donation.repository.AuthoritiesRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class AuthoritiesService {

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	public boolean save(Authorities authorities) throws Exception {
		Authorities a = authoritiesRepository.save(authorities);
		if (a != null) {
			return true;
		}
		return false;
	}

	public boolean createAuthorityForDealer(Dealer dealer) throws Exception {
		Authorities authorities = new Authorities();
		authorities.setUsername(dealer.getUsername());
		authorities.setAuthority(UserRole.ROLE_DEALER.name());
		return save(authorities);
	}

	public boolean createAuthorityForClient(Client client) throws Exception {
		Authorities authorities = new Authorities();
		authorities.setUsername(client.getUsername());
		authorities.setAuthority(UserRole.ROLE_CLIENT.name());
		return save(authorities);
	}

	public Authorities findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return authoritiesRepository.findByUsernameAndArchive(username, archive);
	}
}
