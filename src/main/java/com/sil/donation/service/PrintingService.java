/**
 * 
 */
package com.sil.donation.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sil.donation.entity.Client;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public interface PrintingService {

	/**
	 * Transform Document Object and XSL file to ByteArrayOutputStream for print
	 * @param doc
	 * @param template
	 * @param request
	 * @return Document
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws FOPException
	 */
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template,  HttpServletRequest request) throws TransformerFactoryConfigurationError, TransformerException, FOPException;

	/**
	 * Transform Document Object and XSL file to ByteArrayOutputStream for print
	 * @param document
	 * @param template
	 * @return ByteArrayOutputStream
	 * @throws TransformerException
	 */
	public ByteArrayOutputStream transfromToThermalBytes(Document document, String template) throws TransformerException;

	/**
	 * Generate Document Object from XML
	 * @param xml
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException;

	/**
	 * Generate Client Profile XML Document object
	 * @param client
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateClientProfileDocument(Client client) throws ParserConfigurationException;

	/**
	 * Generate Document object of Donars Transaction report for a client
	 * @param client
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public Document generateDonarsDonationTransactionsReport(Client client) throws ParserConfigurationException;

}
