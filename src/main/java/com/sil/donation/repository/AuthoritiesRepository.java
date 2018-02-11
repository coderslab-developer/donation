package com.sil.donation.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Authorities;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {

	Authorities findByUsernameAndArchive(String username, boolean archive) throws SilException;
}
