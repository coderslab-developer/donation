package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface DealerRepository extends JpaRepository<Dealer, Integer> {

	@Transactional
	Dealer findByUsernameAndArchive(String username, boolean archive) throws SilException;

	Dealer findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException;

	List<Dealer> findAllByArchive(boolean archive) throws SilException;

	List<Dealer> findAllByStatusAndArchive(boolean status, boolean archive) throws SilException;

}
