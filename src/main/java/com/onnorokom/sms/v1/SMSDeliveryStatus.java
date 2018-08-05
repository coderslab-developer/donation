
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "apiKey", "responseId" })
@XmlRootElement(name = "SMSDeliveryStatus")
public class SMSDeliveryStatus {

	protected String apiKey;
	protected String responseId;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String value) {
		this.apiKey = value;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String value) {
		this.responseId = value;
	}

}
