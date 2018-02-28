/**
 * 
 */
package com.sil.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.ClientServiceUpdateInfo;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
public interface ClientServiceUpdateInfoRepository extends JpaRepository<ClientServiceUpdateInfo, Integer> {

}
