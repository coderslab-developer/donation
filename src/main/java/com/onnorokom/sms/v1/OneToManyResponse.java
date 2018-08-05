
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "oneToManyResult" })
@XmlRootElement(name = "OneToManyResponse")
public class OneToManyResponse {

	@XmlElement(name = "OneToManyResult")
	protected String oneToManyResult;

	public String getOneToManyResult() {
		return oneToManyResult;
	}

	public void setOneToManyResult(String value) {
		this.oneToManyResult = value;
	}

}
