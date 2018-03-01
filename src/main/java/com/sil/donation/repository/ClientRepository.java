package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Client;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {

	Client findByUsername(String username) throws SilException;

	List<Client> findAllByArchive(boolean archive) throws SilException;

	List<Client> findAllByDealerId(Integer dealerId) throws SilException;

	List<Client> findByDealerIdAndArchive(Integer dealerId, boolean archive) throws SilException;

	List<Client> findByDealerIdAndStatusAndArchive(Integer dealerId, boolean status, boolean archive) throws SilException;

	List<Client> findByStatusAndArchive(boolean status, boolean archive) throws SilException;

	Client findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException;

	Client findByUsernameAndArchive(String username, boolean archive) throws SilException;

	List<Client> findAllByDealerIdAndStatusAndSmsServiceAndArchive(Integer dealerId, boolean status, boolean smsService, boolean archive) throws SilException;

	List<Client> findAllBySmsServiceAndStatusAndArchive(boolean smsService, boolean status, boolean archive) throws SilException;

}
