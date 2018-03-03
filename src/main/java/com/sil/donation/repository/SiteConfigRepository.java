/**
 * 
 */
package com.sil.donation.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface SiteConfigRepository extends JpaRepository<SiteConfig, Long> {

	public SiteConfig findByUsername(String username) throws SilException;
}
