
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsSms", propOrder = { "mobileNumber", "smsText", "type" })
public class WsSms {

	@XmlElement(name = "MobileNumber")
	protected String mobileNumber;
	@XmlElement(name = "SmsText")
	protected String smsText;
	@XmlElement(name = "Type")
	protected String type;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String value) {
		this.mobileNumber = value;
	}

	public String getSmsText() {
		return smsText;
	}

	public void setSmsText(String value) {
		this.smsText = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String value) {
		this.type = value;
	}

}
