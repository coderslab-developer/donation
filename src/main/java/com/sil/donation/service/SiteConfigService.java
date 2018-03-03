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

	public boolean save(SiteConfig siteConfig) {
		SiteConfig sc = siteConfigRepository.save(siteConfig);
		return sc != null ? true : false;
	}

	public SiteConfig findByUsername(String username) throws SilException{
		return siteConfigRepository.findByUsername(username);
	}
}
