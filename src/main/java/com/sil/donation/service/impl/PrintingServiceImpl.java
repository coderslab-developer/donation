/**
 * 
 */
package com.sil.donation.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.PrintingService;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class PrintingServiceImpl implements PrintingService {

	private static final Logger logger = LoggerFactory.getLogger(PrintingServiceImpl.class);

	@Autowired
	private ClientService clientService;

	@Override
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(template);

		Source xslSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
		if (transformer == null) {
			throw new TransformerException("File not found: " + template);
		}

		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(new DOMSource(doc), res);
		return out;
	}

	@Override
	public Document generateDealerProfileDocument(Dealer dealer) throws ParserConfigurationException {
		List<Client> clients = null;
		try {
			clients = clientService.findAllByDealerId(dealer.getDealerId());
		} catch (SilException e) {
			if(logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
		if(clients != null) {
			dealer.setActiveClients(clients.stream().filter(c -> Boolean.TRUE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
			dealer.setInactiveClients(clients.stream().filter(c -> Boolean.FALSE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
			dealer.setTotalSellOfSoftware(clients.size());
			dealer.setClients(clients);
			List<Client> renewalClients = new ArrayList<>();
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			for(Client c : clients.stream().filter(c -> Boolean.TRUE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
				cal1.setTime(c.getExpireDate());
				cal2.setTime(new Date());
				if((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
					renewalClients.add(c);
				}
			}
			dealer.setServiceRenewOnThisMonth(renewalClients.size());
		}

		//Generate XML
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		//root element
		Element rootElement = doc.createElement("dealer");
		doc.appendChild(rootElement);

		rootElement.appendChild(getChildElement(doc, "dealerName", dealer.getDealerName()));
		rootElement.appendChild(getChildElement(doc, "photo", dealer.getPhoto() == null? "" : dealer.getPhoto()));
		rootElement.appendChild(getChildElement(doc, "mobile", dealer.getMobile()));
		rootElement.appendChild(getChildElement(doc, "email", dealer.getEmail()));
		rootElement.appendChild(getChildElement(doc, "address", dealer.getAddress()));
		rootElement.appendChild(getChildElement(doc, "status", dealer.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		rootElement.appendChild(getChildElement(doc, "registerDate", String.valueOf(dealer.getRegisterDate())));
		rootElement.appendChild(getChildElement(doc, "totalSellOfSoftware", String.valueOf(dealer.getTotalSellOfSoftware())));
		rootElement.appendChild(getChildElement(doc, "activeClients", String.valueOf(dealer.getActiveClients())));
		rootElement.appendChild(getChildElement(doc, "inactiveClients", String.valueOf(dealer.getInactiveClients())));
		rootElement.appendChild(getChildElement(doc, "serviceRenewOnThisMonth", String.valueOf(dealer.getServiceRenewOnThisMonth())));

		for(Client client : clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
			rootElement.appendChild(getClient(doc, client));
		}

		return doc;
	}
	
	public Node getClient(Document doc, Client client) {
		Element clientElement = doc.createElement("client");
		clientElement.appendChild(getChildElement(doc, "clientName", client.getClientName()));
		clientElement.appendChild(getChildElement(doc, "mobile", client.getMobile()));
		clientElement.appendChild(getChildElement(doc, "status", client.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		clientElement.appendChild(getChildElement(doc, "expireDate", String.valueOf(client.getExpireDate())));
		return clientElement;
	}

	// Create Element and set its value
	public Node getChildElement(Document doc, String name, String value) {
		Element element = doc.createElement(name);
		element.appendChild(doc.createTextNode(value));
		return element;
	}

}
