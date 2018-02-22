package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSTransaction;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface SMSTransactionRepository extends JpaRepository<SMSTransaction, Integer> {

	public List<SMSTransaction> findByUsername(String username) throws SilException;

	public List<SMSTransaction> findByUsernameOrderByIdDesc(String username) throws SilException;

}
