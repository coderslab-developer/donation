package com.sil.donation.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Admin;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	public Admin findByUsername(String username) throws SilException;

	public Admin findByUsernameAndArchive(String username, boolean archive) throws SilException;
}
