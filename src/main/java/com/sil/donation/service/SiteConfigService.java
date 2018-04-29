/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.SiteConfigRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class SiteConfigService {

	@Autowired SiteConfigRepository siteConfigRepository;

	/**
	 * Save {@link SiteConfig}
	 * @param siteConfig
	 * @return boolean
	 */
	public boolean save(SiteConfig siteConfig) {
		SiteConfig sc = siteConfigRepository.save(siteConfig);
		return sc != null ? true : false;
	}

	/**
	 * Find {@link SiteConfig} by username
	 * @param username
	 * @return {@link SiteConfig}
	 * @throws SilException
	 */
	public SiteConfig findByUsername(String username) throws SilException{
		return siteConfigRepository.findByUsername(username);
	}
}
