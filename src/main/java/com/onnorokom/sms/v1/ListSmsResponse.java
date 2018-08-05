
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "listSmsResult" })
@XmlRootElement(name = "ListSmsResponse")
public class ListSmsResponse {

	@XmlElement(name = "ListSmsResult")
	protected String listSmsResult;

	public String getListSmsResult() {
		return listSmsResult;
	}

	public void setListSmsResult(String value) {
		this.listSmsResult = value;
	}

}
