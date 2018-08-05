
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "oneToOneBulkResult" })
@XmlRootElement(name = "OneToOneBulkResponse")
public class OneToOneBulkResponse {

	@XmlElement(name = "OneToOneBulkResult")
	protected String oneToOneBulkResult;

	public String getOneToOneBulkResult() {
		return oneToOneBulkResult;
	}

	public void setOneToOneBulkResult(String value) {
		this.oneToOneBulkResult = value;
	}

}
