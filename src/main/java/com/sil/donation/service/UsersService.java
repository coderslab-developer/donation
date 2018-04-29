package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.UserAuthorities;
import com.sil.donation.repository.UsersRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	/**
	 * Save {@link Users}
	 * @param users
	 * @return boolean
	 * @throws SilException
	 */
	public boolean save(Users users) throws SilException {
		Users u = usersRepository.save(users);
		return u != null ? true : false;
	}

	/**
	 * Save {@link Users} from {@link Dealer}
	 * @param dealer
	 * @return boolean
	 * @throws SilException
	 */
	public boolean createUsersFromDealer(Dealer dealer) throws SilException {
		Users users = new Users();
		users.setAuthority(UserAuthorities.ROLE_DEALER.name());
		users.setEmail(dealer.getEmail());
		users.setEnabled(dealer.isStatus());
		users.setPassword(dealer.getPassword());
		users.setUsername(dealer.getUsername());
		return save(users);
	}

	/**
	 * Save {@link Users} from {@link Client}k
	 * @param client
	 * @return boolean
	 * @throws SilException
	 */
	public boolean createUsersFromClient(Client client) throws SilException {
		Users users = new Users();
		users.setAuthority(UserAuthorities.ROLE_CLIENT.name());
		users.setEmail(client.getEmail());
		users.setEnabled(client.isStatus());
		users.setPassword(client.getPassword());
		users.setUsername(client.getUsername());
		return save(users);
	}

	/**
	 * Find {@link Users} by username and archive
	 * @param username
	 * @param archive
	 * @return {@link Users}
	 * @throws SilException
	 */
	public Users findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return usersRepository.findByUsernameAndArchive(username, archive);
	}

	/**
	 * Find all {@link Users}
	 * @return List<{@link Users}>
	 */
	public List<Users> findAll() {
		return usersRepository.findAll();
	}

	/**
	 * Find all {@link Users} by archive
	 * @param archive
	 * @return List<{@link Users}>
	 * @throws SilException
	 */
	public List<Users> findAllByArchive(boolean archive) throws SilException {
		return usersRepository.findAllByArchive(archive);
	}
}
