/**
 * 
 */
package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSNotifier;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface SMSNotifierRepository extends JpaRepository<SMSNotifier, Long> {

	public List<SMSNotifier> findByUsernameAndStatusOrderByIdDesc(String username, boolean status) throws SilException;

	public List<SMSNotifier> findAllByUsernameAndStatus(String username, boolean status) throws SilException;
}
