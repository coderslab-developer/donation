
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userName", "userPassword", "mobileNumber", "smsText", "type", "maskName",
		"campaignName" })
@XmlRootElement(name = "OneToOne")
public class OneToOne {

	protected String userName;
	protected String userPassword;
	protected String mobileNumber;
	protected String smsText;
	protected String type;
	protected String maskName;
	protected String campaignName;

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

	public String getMaskName() {
		return maskName;
	}

	public void setMaskName(String value) {
		this.maskName = value;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String value) {
		this.campaignName = value;
	}

}
