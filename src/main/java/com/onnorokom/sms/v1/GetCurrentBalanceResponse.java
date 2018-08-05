
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getCurrentBalanceResult" })
@XmlRootElement(name = "GetCurrentBalanceResponse")
public class GetCurrentBalanceResponse {

	@XmlElement(name = "GetCurrentBalanceResult")
	protected String getCurrentBalanceResult;

	public String getGetCurrentBalanceResult() {
		return getCurrentBalanceResult;
	}

	public void setGetCurrentBalanceResult(String value) {
		this.getCurrentBalanceResult = value;
	}

}
