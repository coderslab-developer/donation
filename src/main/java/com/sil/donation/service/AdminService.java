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

	public boolean save(Admin admin) {
		Admin a = adminRepository.save(admin);
		if (a != null) {
			return true;
		}
		return false;
	}

	public Admin findByUsername(String username) throws SilException {
		return adminRepository.findByUsername(username);
	}

}
