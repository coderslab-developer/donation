/**
 * 
 */
package com.sil.donation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.ClientServiceUpdateInfo;
import com.sil.donation.repository.ClientServiceUpdateInfoRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class ClientServiceUpdateInfoService {

	@Autowired private ClientServiceUpdateInfoRepository clientServiceUpdateInfoRepository;

	public boolean save(ClientServiceUpdateInfo clientServiceUpdateInfo) {
		ClientServiceUpdateInfo csui = clientServiceUpdateInfoRepository.save(clientServiceUpdateInfo);
		return csui == null ? false : true;
	}
}
