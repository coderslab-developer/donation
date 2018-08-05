
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "apiKey", "wsSmses", "maskName", "campaignName" })
@XmlRootElement(name = "ListSms")
public class ListSms {

	protected String apiKey;
	protected ArrayOfWsSms wsSmses;
	protected String maskName;
	protected String campaignName;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String value) {
		this.apiKey = value;
	}

	public ArrayOfWsSms getWsSmses() {
		return wsSmses;
	}

	public void setWsSmses(ArrayOfWsSms value) {
		this.wsSmses = value;
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
