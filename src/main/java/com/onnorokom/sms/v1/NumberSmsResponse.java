
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "numberSmsResult" })
@XmlRootElement(name = "NumberSmsResponse")
public class NumberSmsResponse {

	@XmlElement(name = "NumberSmsResult")
	protected String numberSmsResult;

	public String getNumberSmsResult() {
		return numberSmsResult;
	}

	public void setNumberSmsResult(String value) {
		this.numberSmsResult = value;
	}

}
