
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "deliveryStatusResult" })
@XmlRootElement(name = "DeliveryStatusResponse")
public class DeliveryStatusResponse {

	@XmlElement(name = "DeliveryStatusResult")
	protected String deliveryStatusResult;

	public String getDeliveryStatusResult() {
		return deliveryStatusResult;
	}

	public void setDeliveryStatusResult(String value) {
		this.deliveryStatusResult = value;
	}

}
