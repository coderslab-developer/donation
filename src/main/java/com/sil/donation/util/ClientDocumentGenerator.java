package com.sil.donation.util;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SiteConfig;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class ClientDocumentGenerator {

	public Document clientProfileDocument(Client client, SiteConfig siteConfig) throws ParserConfigurationException {
		//Generate XML
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		//root element
		Element rootElement = doc.createElement("client");
		doc.appendChild(rootElement);

//		if(siteConfig != null && siteConfig.isEnableLogo() && (!siteConfig.getLogo().isEmpty() || siteConfig.getLogo() != null)) {
//			rootElement.appendChild(getChildElement(doc, "siteLogo", siteConfig.getLogo() == null ? "site_logo.png" : siteConfig.getLogo()));
//		}else {
			rootElement.appendChild(getChildElement(doc, "siteLogo", "site_logo.png"));
//		}

		rootElement.appendChild(getChildElement(doc, "clientName", client.getClientName() == null ? "" : client.getClientName()));
		rootElement.appendChild(getChildElement(doc, "email", client.getEmail() == null ? "" : client.getEmail()));
		rootElement.appendChild(getChildElement(doc, "mobile", client.getMobile() == null ? "" : client.getMobile()));
		rootElement.appendChild(getChildElement(doc, "fax", client.getFax() == null ? "" : client.getFax()));
		rootElement.appendChild(getChildElement(doc, "website", client.getWebsite() == null ? "" : client.getWebsite()));
		rootElement.appendChild(getChildElement(doc, "registerDate", client.getRegisterDate() == null ? "" : String.valueOf(client.getRegisterDate())));
		rootElement.appendChild(getChildElement(doc, "expireDate", client.getExpireDate() == null ? "" : String.valueOf(client.getExpireDate())));
		rootElement.appendChild(getChildElement(doc, "address", client.getAddress()== null ? "" : client.getAddress()));
		rootElement.appendChild(getChildElement(doc, "status", client.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		rootElement.appendChild(getChildElement(doc, "photo", client.getPhoto() == null ? "" : client.getPhoto()));
		rootElement.appendChild(getChildElement(doc, "smsService", client.isSmsService() == Boolean.TRUE ? "Active" : "Inactive"));
		rootElement.appendChild(getChildElement(doc, "totalDonars", client.getTotalDonar() == null ? "0" : String.valueOf(client.getTotalDonar())));
		rootElement.appendChild(getChildElement(doc, "activeDonars", client.getActiveDonar() == null ? "" : String.valueOf(client.getActiveDonar())));
		rootElement.appendChild(getChildElement(doc, "inactiveDonars", client.getInactiveDonar() == null ? "" : String.valueOf(client.getInactiveDonar())));

		rootElement.appendChild(getDonarsElement(doc, client.getDonars()));

		return doc;
	}

	//get Donars
	private Node getDonarsElement(Document doc, List<Donar> donars) {
		Element donarsElement = doc.createElement("donars");
		int index = 1;
		for(Donar donar : donars) {
			donarsElement.appendChild(getDonarElement(doc, donar, index));
			index++;
		}
		return donarsElement;
	}

	//get single donar
	public Node getDonarElement(Document doc, Donar donar, int index) {
		Element donarElement = doc.createElement("donar");
		donarElement.appendChild(getChildElement(doc, "no", String.valueOf(index)));
		donarElement.appendChild(getChildElement(doc, "donarName", donar.getDonarName() == null ? "" : donar.getDonarName()));
		donarElement.appendChild(getChildElement(doc, "categoryName", donar.getCategoryName() == null ? "" : donar.getCategoryName()));
		donarElement.appendChild(getChildElement(doc, "mobile", donar.getMobile() == null ? "" : donar.getMobile()));
		donarElement.appendChild(getChildElement(doc, "payableAmount", String.valueOf(donar.getPayableAmount()) == null ? "" : String.valueOf(donar.getPayableAmount())));
		donarElement.appendChild(getChildElement(doc, "status", donar.isStatus() == Boolean.TRUE ? "Active" : "Inactive"));
		donarElement.appendChild(getChildElement(doc, "smsService", donar.isSmsService() == Boolean.TRUE ? "Active" : "Inactive"));
		return donarElement;
	}

	// Create Element and set its value
	private Node getChildElement(Document doc, String name, String value) {
		Element element = doc.createElement(name);
		element.appendChild(doc.createTextNode(value));
		return element;
	}
}
