/**
 * 
 */
package com.sil.donation.service;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import com.sil.donation.entity.Dealer;

/**
 * @author cyclingbd007
 *
 */
@Component
public interface PrintingService {

	/**
	 * Transform dealer profile to thermal print
	 * @param dealer
	 * @param xslTemplate
	 * @return
	 * @throws TransformerException
	 */
	public ByteArrayOutputStream transformDealerToThermal(Dealer dealer, String xslTemplate) throws TransformerException;
	
}
