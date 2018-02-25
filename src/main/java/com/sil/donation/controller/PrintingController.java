/**
 * 
 */
package com.sil.donation.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

import com.sil.donation.entity.Dealer;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.PrintingService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/print")
public class PrintingController {

	private static final Logger logger = LoggerFactory.getLogger(PrintingController.class);

	private static final String DEALER_PROFILE_TEMPALTE = "dealer_profile_template.xsl";

	@Autowired
	private DealerService dealerService;

	@Autowired
	private PrintingService printingService;

	@RequestMapping(value = "/dealer/{dealerId}")
	public ResponseEntity<byte[]> doPrint(@PathVariable("dealerId") Integer dealerId) {
		System.out.println("got it");
		String message = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		// find xsl tempate
		StringBuilder template = null;
		try {
			template = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
					.append(File.separator).append("xsl").append(File.separator).append(DEALER_PROFILE_TEMPALTE);
		} catch (URISyntaxException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
		if (template == null) {
			message = "No Template found";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// get dealer
		Dealer dealer = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
		} catch (SilException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
		if (dealer == null) {
			message = "No Dealer available";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		byte[] byt = null;
		ByteArrayOutputStream out = null;

		Document doc = null;
		try {
			doc = printingService.generateDealerProfileDocument(dealer);
		} catch (ParserConfigurationException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
		if (doc == null) {
			message = "Can't generate XML file for document";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			out = printingService.transfromToPDFBytes(doc, template.toString());
		} catch (FOPException | TransformerFactoryConfigurationError | TransformerException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
			message = "Can't Transform XML file to PDF";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// final work
		if (out == null) {
			message = "Can't generate PDF to print";
			byt = message.getBytes();
		} else {
			byt = out.toByteArray();
			headers.setContentType(new MediaType("application", "pdf"));
		}
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
