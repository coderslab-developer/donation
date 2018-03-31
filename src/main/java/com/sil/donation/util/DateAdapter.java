/**
 * 
 */
package com.sil.donation.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Zubayer Ahamed
 *
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public String marshal(Date v) throws Exception {
		return SDF.format(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return SDF.parse(v);
	}

}
