package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.repository.AuthoritiesRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class AuthoritiesService {

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	/**
	 * Save {@link Authorities}
	 * @param authorities
	 * @return boolean
	 * @throws Exception
	 */
	public boolean save(Authorities authorities) throws Exception {
		Authorities a = authoritiesRepository.save(authorities);
		return a != null ? true : false;
	}

	/**
	 * Save {@link Authorities} for {@link Dealer}
	 * @param dealer
	 * @return boolean
	 * @throws Exception
	 */
	public boolean createAuthorityForDealer(Dealer dealer) throws Exception {
		Authorities authorities = new Authorities();
		authorities.setUsername(dealer.getUsername());
		authorities.setAuthority(UserAuthorities.ROLE_DEALER.name());
		return save(authorities);
	}

	/**
	 * Save {@link Authorities} for {@link Client}
	 * @param client
	 * @return boolean
	 * @throws Exception
	 */
	public boolean createAuthorityForClient(Client client) throws Exception {
		Authorities authorities = new Authorities();
		authorities.setUsername(client.getUsername());
		authorities.setAuthority(UserAuthorities.ROLE_CLIENT.name());
		return save(authorities);
	}

	/**
	 * Find {@link Authorities} by Username and Archive
	 * @param username
	 * @param archive
	 * @return {@link Authorities}
	 * @throws SilException
	 */
	public Authorities findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return authoritiesRepository.findByUsernameAndArchive(username, archive);
	}
}
