
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "oneToOneResult" })
@XmlRootElement(name = "OneToOneResponse")
public class OneToOneResponse {

	@XmlElement(name = "OneToOneResult")
	protected String oneToOneResult;

	public String getOneToOneResult() {
		return oneToOneResult;
	}

	public void setOneToOneResult(String value) {
		this.oneToOneResult = value;
	}

}
