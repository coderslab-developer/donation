package com.sil.donation.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.apache.log4j.Logger;

import com.onnorokom.sms.v1.SendSms;
import com.onnorokom.sms.v1.SendSmsSoap;

public class OnnorkomObjectBuilder {
	private static final Logger logger = Logger.getLogger(OnnorkomObjectBuilder.class);

	public static SendSmsSoap getSMSPort(String endPoint) {
		SendSms port = new SendSms();

		port.setHandlerResolver(new HandlerResolver() {

			@SuppressWarnings("rawtypes")
			@Override
			public List<Handler> getHandlerChain(PortInfo arg0) {
				List<Handler> list = new ArrayList<>();
				//list.add(new SOAPLoggingHandler());
				// list.add(new HttpHeaderInterceptor(headerValue));
				return list;
			}
		});

		SendSmsSoap smsport = port.getSendSmsSoap();
		updateEndPoint((BindingProvider) smsport, endPoint);

		return smsport;
	}

	public static void updateEndPoint(BindingProvider bindingProvider, String endPoint) {

		logger.info(String.format("Default endpoint_address: %s", bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY)));
		if (endPoint != null) {
			bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
			logger.info(String.format("Target endpoint_address: %s", bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY)));
		}
	}

}
