package com.sil.donation.util;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.util.Strings;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.SiteConfig;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class DealerDocumentGenerator {

	public Document dealerProfileDocument(Dealer dealer, SiteConfig siteConfig) throws ParserConfigurationException {
		//Generate XML
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		//root element
		Element rootElement = doc.createElement("dealer");
		doc.appendChild(rootElement);

//		if(siteConfig != null && siteConfig.isEnableLogo() && Strings.isNullOrEmpty(siteConfig.getLogo())) {
//			rootElement.appendChild(getChildElement(doc, "siteLogo", siteConfig.getLogo() == "" ? "site_logo.png" : siteConfig.getLogo()));
//		}else {
			rootElement.appendChild(getChildElement(doc, "siteLogo", "site_logo.png"));
//		}

		rootElement.appendChild(getChildElement(doc, "dealerName", dealer.getDealerName() == null ? "" : dealer.getDealerName()));
		rootElement.appendChild(getChildElement(doc, "dealerPhoto", dealer.getPhoto() == null ? "" : dealer.getPhoto()));
		rootElement.appendChild(getChildElement(doc, "mobile", dealer.getMobile() == null ? "" : dealer.getMobile()));
		rootElement.appendChild(getChildElement(doc, "email", dealer.getEmail() == null ? "" : dealer.getEmail()));
		rootElement.appendChild(getChildElement(doc, "address", dealer.getAddress() == null ? "" : dealer.getAddress()));
		rootElement.appendChild(getChildElement(doc, "status", dealer.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		rootElement.appendChild(getChildElement(doc, "registerDate", dealer.getRegisterDate() == null ? "" : String.valueOf(dealer.getRegisterDate())));
		rootElement.appendChild(getChildElement(doc, "totalSellOfSoftware", String.valueOf(dealer.getTotalSellOfSoftware()) == null ? "0" : String.valueOf(dealer.getTotalSellOfSoftware())));
		rootElement.appendChild(getChildElement(doc, "activeClients", String.valueOf(dealer.getActiveClients())==null? "0" : String.valueOf(dealer.getActiveClients())));
		rootElement.appendChild(getChildElement(doc, "inactiveClients", String.valueOf(dealer.getInactiveClients()) == null ? "0" : String.valueOf(dealer.getInactiveClients())));
		rootElement.appendChild(getChildElement(doc, "serviceRenewOnThisMonth", String.valueOf(dealer.getServiceRenewOnThisMonth()) == null ? "0" : String.valueOf(dealer.getServiceRenewOnThisMonth())));

		rootElement.appendChild(getClients(doc, dealer.getClients()));

		return doc;
	}

	//get Clients
	private Node getClients(Document doc, List<Client> clients) {
		Element clientElement = doc.createElement("clients");
		int index = 1;
		for(Client client : clients) {
			clientElement.appendChild(getClient(doc, client, index));
			index++;
		}
		return clientElement;
	}

	//get single Client
	private Node getClient(Document doc, Client client, int index) {
		Element element = doc.createElement("client");
		element.appendChild(getChildElement(doc, "no", String.valueOf(index)));
		element.appendChild(getChildElement(doc, "clientName", client.getClientName() == null ? "" : client.getClientName()));
		element.appendChild(getChildElement(doc, "mobile", client.getMobile() == null ? "" : client.getMobile()));
		element.appendChild(getChildElement(doc, "status", client.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		element.appendChild(getChildElement(doc, "registerDate", String.valueOf(client.getRegisterDate()) == null ? "" : String.valueOf(client.getRegisterDate())));
		element.appendChild(getChildElement(doc, "expireDate", String.valueOf(client.getExpireDate()) == null ? "" : String.valueOf(client.getExpireDate())));
		return element;
	}

	// Create Element and set its value
	private Node getChildElement(Document doc, String name, String value) {
		Element element = doc.createElement(name);
		element.appendChild(doc.createTextNode(value));
		return element;
	}
}
