/**
 * 
 */
package com.sil.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSKeyGenerator;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
public interface SMSKeyGeneratorRepository extends JpaRepository<SMSKeyGenerator, Long> {

}
