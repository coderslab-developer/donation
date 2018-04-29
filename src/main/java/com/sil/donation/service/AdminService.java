/**
 * Copyrights Zubayer Ahamed
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Admin;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.AdminRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	/**
	 * Save {@link Admin} Info
	 * @param admin
	 * @return boolean
	 */
	public boolean save(Admin admin) {
		Admin a = adminRepository.save(admin);
		return a != null ? true : false;
	}

	/**
	 * Find {@link Admin} by Unsername
	 * @param username
	 * @return {@link Admin}
	 * @throws SilException
	 */
	public Admin findByUsername(String username) throws SilException {
		return adminRepository.findByUsername(username);
	}

	/**
	 * Find {@link Admin} by Username and Archive
	 * @param username
	 * @param archive
	 * @return {@link Admin}
	 * @throws SilException
	 */
	public Admin findByUsernameAndArchive(String username, boolean archive) throws SilException {
		return adminRepository.findByUsernameAndArchive(username, archive);
	}

}
