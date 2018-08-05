
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userName", "userPassword" })
@XmlRootElement(name = "GetMask")
public class GetMask {

	protected String userName;
	protected String userPassword;

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

}
