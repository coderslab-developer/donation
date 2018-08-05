
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageHeader", propOrder = { "userName", "userPassword", "marskText", "campingName" })
public class MessageHeader {

	@XmlElement(name = "UserName")
	protected String userName;
	@XmlElement(name = "UserPassword")
	protected String userPassword;
	@XmlElement(name = "MarskText")
	protected String marskText;
	@XmlElement(name = "CampingName")
	protected String campingName;

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

	public String getMarskText() {
		return marskText;
	}

	public void setMarskText(String value) {
		this.marskText = value;
	}

	public String getCampingName() {
		return campingName;
	}

	public void setCampingName(String value) {
		this.campingName = value;
	}

}
