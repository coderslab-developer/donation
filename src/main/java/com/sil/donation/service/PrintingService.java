/**
 * 
 */
package com.sil.donation.service;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public interface PrintingService {

	/**
	 * Transform any document object to Byte Array output Stream for print
	 * @param doc
	 * @param template
	 * @param request
	 * @return Document
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws FOPException
	 */
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template,  HttpServletRequest request)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException;

	/**
	 * Generate Dealer Profile XML Document object
	 * @param dealer
	 * @param request
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateDealerProfileDocument(Dealer dealer) throws ParserConfigurationException;

	/**
	 * Generate Client Profile XML Document object
	 * @param client
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateClientProfileDocument(Client client) throws ParserConfigurationException;

	/**
	 * Generate Donar Profile XML Document object
	 * @param donar
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateDonarProfileDocument(Donar donar) throws ParserConfigurationException;

	/**
	 * Generate Admin Profile XML document object
	 * @param admin
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateAdminProfileDocument(Admin admin) throws ParserConfigurationException;

}
