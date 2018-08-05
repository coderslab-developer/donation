
package com.onnorokom.sms.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "messageHeader", "wsSmses" })
@XmlRootElement(name = "OneToOneBulk")
public class OneToOneBulk {

	protected MessageHeader messageHeader;
	protected ArrayOfWsSms wsSmses;

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader value) {
		this.messageHeader = value;
	}

	public ArrayOfWsSms getWsSmses() {
		return wsSmses;
	}

	public void setWsSmses(ArrayOfWsSms value) {
		this.wsSmses = value;
	}

}
