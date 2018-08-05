
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getBalanceResult" })
@XmlRootElement(name = "GetBalanceResponse")
public class GetBalanceResponse {

	@XmlElement(name = "GetBalanceResult")
	protected String getBalanceResult;

	public String getGetBalanceResult() {
		return getBalanceResult;
	}

	public void setGetBalanceResult(String value) {
		this.getBalanceResult = value;
	}

}
