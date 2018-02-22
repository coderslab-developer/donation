/**
 * 
 */
package com.sil.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSKeyGenerator;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
public interface SMSKeyGeneratorRepository extends JpaRepository<SMSKeyGenerator, Long> {

	public SMSKeyGenerator findBySmsKeyAndStatus(String smsKey, boolean status) throws SilException;
}
