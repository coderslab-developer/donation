
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getAllMaskResult" })
@XmlRootElement(name = "GetAllMaskResponse")
public class GetAllMaskResponse {

	@XmlElement(name = "GetAllMaskResult")
	protected String getAllMaskResult;

	public String getGetAllMaskResult() {
		return getAllMaskResult;
	}

	public void setGetAllMaskResult(String value) {
		this.getAllMaskResult = value;
	}

}
