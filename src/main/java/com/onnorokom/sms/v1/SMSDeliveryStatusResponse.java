
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "smsDeliveryStatusResult" })
@XmlRootElement(name = "SMSDeliveryStatusResponse")
public class SMSDeliveryStatusResponse {

	@XmlElement(name = "SMSDeliveryStatusResult")
	protected String smsDeliveryStatusResult;

	public String getSMSDeliveryStatusResult() {
		return smsDeliveryStatusResult;
	}

	public void setSMSDeliveryStatusResult(String value) {
		this.smsDeliveryStatusResult = value;
	}

}
