package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Integer> {

	@Transactional
	Users findByUsernameAndArchive(String username, boolean archive) throws SilException;

	List<Users> findAllByArchive(boolean archive) throws SilException;
}
