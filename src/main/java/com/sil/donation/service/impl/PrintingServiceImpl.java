/**
 * 
 */
package com.sil.donation.service.impl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sil.donation.entity.Dealer;
import com.sil.donation.service.PrintingService;

/**
 * @author cyclingbd007
 *
 */
@Service
public class PrintingServiceImpl implements PrintingService{

	private static final Logger logger = LoggerFactory.getLogger(PrintingServiceImpl.class);
	
	@Override
	public ByteArrayOutputStream transformDealerToThermal(Dealer dealer, String xslTemplate) throws TransformerException {
		return transfromToThermalBytes(getDomSourceForDealer(dealer), xslTemplate);
	}

	private Document getDomSourceForDealer(Dealer dealer) {
		Document doc = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException pce) {
			logger.error(pce.getMessage(), pce);
		}
		
		//root elements
		Element rootElement = doc.createElement("dealer");
		doc.appendChild(rootElement);
		
		rootElement.appendChild(doc.createElement("dealerId").appendChild(doc.createTextNode(String.valueOf(dealer.getDealerId()))));
		rootElement.appendChild(doc.createElement("dealerName").appendChild(doc.createTextNode(dealer.getDealerName())));
		
		return doc;
	}

	private ByteArrayOutputStream transfromToThermalBytes(Document doc, String xslTemplate) throws TransformerFactoryConfigurationError, TransformerException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File("src//main//resources//static//xsl//" + xslTemplate);
		Source xsltSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSrc);
		transformer.transform(new DOMSource(doc), new StreamResult(out));
		return out;
	}

}
