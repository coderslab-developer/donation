/**
 * 
 */
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
import com.sil.donation.entity.Donation;
import com.sil.donation.entity.SiteConfig;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class DonarsTransactionDocumentGenerator {

	public Document generateDonarsTransactionReport(Client client, SiteConfig siteConfig) throws ParserConfigurationException {
		//Generate XML
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		//root element
		Element rootElement = doc.createElement("client");
		doc.appendChild(rootElement);

		if(siteConfig != null && siteConfig.isEnableLogo() && (!siteConfig.getLogo().isEmpty() || siteConfig.getLogo() != null)) {
			rootElement.appendChild(getChildElement(doc, "siteLogo", siteConfig.getLogo() == null ? "site_logo.png" : siteConfig.getLogo()));
		}else {
			rootElement.appendChild(getChildElement(doc, "siteLogo", "site_logo.png"));
		}

		rootElement.appendChild(getChildElement(doc, "clientName", client.getClientName() == null ? "" : client.getClientName()));
		rootElement.appendChild(getChildElement(doc, "mobile", client.getMobile() == null ? "" : client.getMobile()));
		rootElement.appendChild(getChildElement(doc, "address", client.getAddress() == null ? "" : client.getAddress()));
		
		if(!client.getDonations().isEmpty()) {
			rootElement.appendChild(getDonationsElement(doc, client.getDonations()));
		}

		double totalPayableAmount = 0;
		double totalPaid = 0;
		double totalDue = 0;
		for(Donation donation : client.getDonations()) {
			totalPayableAmount = totalPayableAmount + donation.getPayableAmount();
			totalPaid = totalPaid + donation.getPaid();
			totalDue = totalDue + donation.getDue();
		}
		rootElement.appendChild(getChildElement(doc, "totalPayableAmount", String.valueOf(totalPayableAmount)));
		rootElement.appendChild(getChildElement(doc, "totalPaid", String.valueOf(totalPaid)));
		rootElement.appendChild(getChildElement(doc, "totalDue", String.valueOf(totalDue)));
		return doc;
	}

	public Node getDonationsElement(Document doc, List<Donation> donations) {
		Element donationsElement = doc.createElement("donations");
		int i = 1;
		for(Donation donation : donations) {
			donationsElement.appendChild(getDonationElement(doc, donation, i));
			i++;
		}
		return donationsElement;
	}

	public Node getDonationElement(Document doc, Donation donation, int position) {
		Element donationsElement = doc.createElement("donation");
		donationsElement.appendChild(getChildElement(doc, "no", String.valueOf(position)));
		donationsElement.appendChild(getChildElement(doc, "donarName", donation.getDonarName() == null ? "": donation.getDonarName()));
		donationsElement.appendChild(getChildElement(doc, "payableAmount", donation.getPayableAmount() == 0 ? "0": String.valueOf(donation.getPayableAmount())));
		donationsElement.appendChild(getChildElement(doc, "paid", donation.getPaid() == 0 ? "0": String.valueOf(donation.getPaid())));
		donationsElement.appendChild(getChildElement(doc, "due", donation.getDue() == 0 ? "0": String.valueOf(donation.getDue())));
		donationsElement.appendChild(getChildElement(doc, "payDate", donation.getPayDate() == null ? "": String.valueOf(donation.getPayDate())));
		return donationsElement;
	}

	// Create Element and set its value
	private Node getChildElement(Document doc, String name, String value) {
		Element element = doc.createElement(name);
		element.appendChild(doc.createTextNode(value));
		return element;
	}
}
