
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userName", "userPassword", "responseId" })
@XmlRootElement(name = "DeliveryStatus")
public class DeliveryStatus {

	protected String userName;
	protected String userPassword;
	protected String responseId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String value) {
		this.userName = value;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String value) {
		this.userPassword = value;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String value) {
		this.responseId = value;
	}

}
