
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "apiKey", "messageText", "numberList", "smsType", "maskName", "campaignName" })
@XmlRootElement(name = "NumberSms")
public class NumberSms {

	protected String apiKey;
	protected String messageText;
	protected String numberList;
	protected String smsType;
	protected String maskName;
	protected String campaignName;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String value) {
		this.apiKey = value;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String value) {
		this.messageText = value;
	}

	public String getNumberList() {
		return numberList;
	}

	public void setNumberList(String value) {
		this.numberList = value;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String value) {
		this.smsType = value;
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
