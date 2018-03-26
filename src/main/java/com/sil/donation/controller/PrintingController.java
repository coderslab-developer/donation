package com.sil.donation.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donation;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.AdminService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.DonationService;
import com.sil.donation.service.PrintingService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/print")
public class PrintingController {

	private static final Logger logger = LoggerFactory.getLogger(PrintingController.class);

	private static final String ADMIN_PROFILE_TEMPALTE = "admin_profile_template.xsl";
	private static final String DEALER_PROFILE_TEMPALTE = "dealer_profile_template.xsl";
	private static final String CLIENT_PROFILE_TEMPALTE = "client_profile_template.xsl";
	private static final String DONAR_PROFILE_TEMPALTE = "donar_profile_template.xsl";
	private static final String DONAR_TRANSACTION_REPORT_TEMPLATE = "donar_transaction_report_template.xsl";
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired private DealerService dealerService;
	@Autowired private PrintingService printingService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private AdminService adminService;
	@Autowired private DonationService donationService;

	@RequestMapping(value = "/dealer/{dealerId}")
	public ResponseEntity<byte[]> printDealer(@PathVariable("dealerId") Integer dealerId, HttpServletRequest request) {
		String message = "Something went wrong";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		// find xsl tempate
		StringBuilder template = null;
		try {
			template = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
					.append(File.separator).append("xsl").append(File.separator)
					.append(DEALER_PROFILE_TEMPALTE);
		} catch (URISyntaxException e) {
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
			logger.error(e.getMessage(), e);
		}
		if (doc == null) {
			message = "Can't generate XML file for document";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (FOPException | TransformerFactoryConfigurationError | TransformerException e) {
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
	
	@RequestMapping(value = "/client/{clientId}")
	public ResponseEntity<byte[]> printClient(@PathVariable("clientId") Integer clientId, HttpServletRequest request) {
		String message = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		// find xsl tempate
		StringBuilder template = null;
		try {
			template = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
					.append(File.separator).append("xsl").append(File.separator)
					.append(CLIENT_PROFILE_TEMPALTE);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
		if (template == null) {
			message = "No Template found";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// get dealer
		Client client = null;
		try {
			client = clientService.findByClientIdAndArchive(clientId, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if (client == null) {
			message = "No Dealer available";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		byte[] byt = null;
		ByteArrayOutputStream out = null;

		Document doc = null;
		try {
			doc = printingService.generateClientProfileDocument(client);
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
		if (doc == null) {
			message = "Can't generate XML file for document";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (FOPException | TransformerFactoryConfigurationError | TransformerException e) {
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

	@RequestMapping(value = "/donarTransactionReport/{donarId}/{startDate}/{endDate}")
	public ResponseEntity<byte[]> printDonarReport(@PathVariable("donarId") Integer donarId, 
			@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, HttpServletRequest request) throws ParseException {
		logger.info("{}", request.getContextPath());
		String message = "Something went wrong";
		byte[] byt = message.getBytes();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		// find xsl tempate
		StringBuilder template = null;
		try {
			template = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
					.append(File.separator).append("xsl").append(File.separator)
					.append(DONAR_TRANSACTION_REPORT_TEMPLATE);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
		if (template == null) {
			message = "No Template found";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		}

		//check date was not empty
		if(startDate == "" || endDate == "") {
			message = "Please check date filed. Something is missing in date input field";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		}

		//get authenticate username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Client client = null;
		try {
			client = clientService.findByUsernameAndArchive(username, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(client == null) {
			message = "You are invalid client";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		}

		Date firstDate = DATE_FORMATTER.parse(startDate);
		Date lastDate = DATE_FORMATTER.parse(endDate);

		if(firstDate.compareTo(lastDate) > 0) {
			firstDate = DATE_FORMATTER.parse(endDate);
			lastDate = DATE_FORMATTER.parse(startDate);
		}
		logger.info(" hi there {}, {}", startDate, endDate);

		List<Donation> donations = donationService.findAllDonationByClientIdAndPayDateBetweenStartDateToEndDate(client.getClientId(), donarId == -1 ? null : donarId, firstDate, lastDate);
		if(donations.isEmpty()) {
			message = "No data found to print";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		}
		try {
			for(Donation d : donations) {
				d.setDonarName(donarService.findByDonarId(d.getDonarId(), false).getDonarName());
			}
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		//set donations list into this client
		client.setDonations(donations);
		logger.info("total donations {}", client.getDonations().size());

		logger.info("last {}, {}, {}", donarId, startDate, endDate);

		ByteArrayOutputStream out = null;

		Document doc = null;
		try {
			doc = printingService.generateDonarsDonationTransactionsReport(client);
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
		if (doc == null) {
			message = "Can't generate XML file for document";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		}

		try {
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (FOPException | TransformerFactoryConfigurationError | TransformerException e) {
			logger.error(e.getMessage(), e);
			message = "Can't Transform XML file to PDF";
			byt = message.getBytes();
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
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
