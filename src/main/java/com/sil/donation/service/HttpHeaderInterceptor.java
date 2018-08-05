package com.sil.donation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class HttpHeaderInterceptor implements SOAPHandler<SOAPMessageContext> {

	Logger logger = Logger.getLogger(HttpHeaderInterceptor.class);

	private String headervalue;

	public HttpHeaderInterceptor() {

	}

	public HttpHeaderInterceptor(String headerValue) {
		this.headervalue = headerValue;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
			@SuppressWarnings("unchecked")
			Map<String, List<String>> headers = (Map<String, List<String>>) context
					.get(MessageContext.HTTP_REQUEST_HEADERS);
			if (headers == null) {
				headers = new HashMap<>();
				List<String> usernamePassword = new ArrayList<>();
				usernamePassword.add("Basic " + this.headervalue);
				headers.put("Authorization", usernamePassword);
				context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
			}
		}
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Get-headers method stub
		return null;
	}

}
