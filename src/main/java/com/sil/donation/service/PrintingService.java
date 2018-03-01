/**
 * 
 */
package com.sil.donation.service;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.sil.donation.entity.Dealer;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public interface PrintingService {

	/**
	 * Transform any document object to Byte Array output Stream for print
	 * 
	 * @param doc
	 * @param template
	 * @return ByteArrayOutputStream
	 */
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException;

	/**
	 * Generate Dealer Profile XML Document object with all information
	 * 
	 * @param dealer
	 * @return Document
	 */
	public Document generateDealerProfileDocument(Dealer dealer) throws ParserConfigurationException;

}
