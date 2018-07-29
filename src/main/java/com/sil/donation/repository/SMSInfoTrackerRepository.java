/**
 * 
 */
package com.sil.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SMSInfoTracker;

/**
 * @author zubayer
 *
 */
@Repository
public interface SMSInfoTrackerRepository extends JpaRepository<SMSInfoTracker, Integer>{

}
