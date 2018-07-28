/**
 * 
 */
package com.sil.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSAccount;

/**
 * @author zubayer
 *
 */
@Repository
public interface SMSAccountRepository extends JpaRepository<SMSAccount, Integer>{

	public SMSAccount findBySecretNameAndActiveAndArchive(String secretName, boolean acive, boolean archive);
	public List<SMSAccount> findAllByArchive(boolean archive);
}
