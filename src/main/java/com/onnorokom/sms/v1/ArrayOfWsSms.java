
package com.onnorokom.sms.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWsSms", propOrder = { "wsSms" })
public class ArrayOfWsSms {

	@XmlElement(name = "WsSms", nillable = true)
	protected List<WsSms> wsSms;

	public List<WsSms> getWsSms() {
		if (wsSms == null) {
			wsSms = new ArrayList<WsSms>();
		}
		return this.wsSms;
	}

	public void setWsSms(List<WsSms> wsSms) {
		this.wsSms = wsSms;
	}

}
