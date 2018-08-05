
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "apiKey" })
@XmlRootElement(name = "GetAllMask")
public class GetAllMask {

	protected String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String value) {
		this.apiKey = value;
	}

}
