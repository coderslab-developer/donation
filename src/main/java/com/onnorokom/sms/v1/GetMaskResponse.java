
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getMaskResult" })
@XmlRootElement(name = "GetMaskResponse")
public class GetMaskResponse {

	@XmlElement(name = "GetMaskResult")
	protected String getMaskResult;

	public String getGetMaskResult() {
		return getMaskResult;
	}

	public void setGetMaskResult(String value) {
		this.getMaskResult = value;
	}

}
