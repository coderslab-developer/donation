package com.sil.donation.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

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
import org.xml.sax.SAXException;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.Donation;
import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.DonationService;
import com.sil.donation.service.PrintingService;
import com.sil.donation.service.SiteConfigService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/print")
public class PrintingController {

	private static final Logger logger = LoggerFactory.getLogger(PrintingController.class);

	private static final String DEALER_PROFILE_TEMPALTE = "dealer_profile_template.xsl";
	private static final String CLIENT_PROFILE_TEMPALTE = "client_profile_template.xsl";
	private static final String DONAR_TRANSACTION_REPORT_TEMPLATE = "donar_transaction_report_template.xsl";
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired private DealerService dealerService;
	@Autowired private PrintingService printingService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private DonationService donationService;
	@Autowired private SiteConfigService siteConfigService;
	@Autowired private CategoryService categoryService;

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
		//get Client list
		List<Client> clients = new ArrayList<>();
		try {
			clients = clientService.findAllByDealerId(dealer.getDealerId());
		} catch (SilException e2) {
			logger.error(e2.getMessage(), e2);
		}
		if(!clients.isEmpty()) {
			dealer.setActiveClients(clients.stream().filter(c -> Boolean.TRUE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
			dealer.setInactiveClients(clients.stream().filter(c -> Boolean.FALSE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
			dealer.setTotalSellOfSoftware(clients.size());
			dealer.setClients(clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()));
			List<Client> renewalClients = new ArrayList<>();
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			for(Client c : clients.stream().filter(c -> Boolean.TRUE == c.isStatus() && Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
				cal1.setTime(c.getExpireDate());
				cal2.setTime(new Date());
				if((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
					renewalClients.add(c);
				}
			}
			dealer.setServiceRenewOnThisMonth(renewalClients.size());
		}

		dealer.setSiteLogo("site_logo.png");
		dealer.setReportName("Deaaler Profile Info");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(siteConfig != null && siteConfig.getLogo() != null && Boolean.TRUE == siteConfig.isEnableLogo()) {
			dealer.setSiteLogo(siteConfig.getLogo());
		}

		byte[] byt = null;
		ByteArrayOutputStream out = null;
		Document doc = null;
		try {
			String xml = parseXMLString(dealer);
			doc = printingService.getDomSourceForXML(xml);
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (JAXBException | ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError | TransformerException e1) {
			logger.error(e1.getMessage(), e1);
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

		//get donars
		List<Donar> donars = null;
		try {
			client.setDealerName(dealerService.findByDealerIdAndArchive(client.getDealerId(), false).getDealerName());
			donars = donarService.findAllByClientId(client.getClientId());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(donars != null) {
			donars.stream().forEach(d -> {
				try {
					d.setCategoryName(categoryService.findByCategoryIdAndArchive(d.getCategoryId(), false).getName());
				} catch (SilException e) {
					logger.error(e.getMessage(), e);
				}
			});
			client.setDonars(donars.stream().filter(d -> Boolean.FALSE == d.isArchive()).collect(Collectors.toList()));
			client.setTotalDonar(donars.stream().filter(d -> Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());
			client.setActiveDonar(donars.stream().filter(d -> Boolean.TRUE == d.isStatus() && Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());
			client.setInactiveDonar(donars.stream().filter(d -> Boolean.FALSE == d.isStatus() && Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());
			client.setNumberOfPayeeDonarInThisMonth(0);
		}

		client.setSiteLogo("site_logo.png");
		client.setReportName("Deaaler Profile Info");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(siteConfig != null && siteConfig.getLogo() != null && Boolean.TRUE == siteConfig.isEnableLogo()) {
			client.setSiteLogo(siteConfig.getLogo());
		}

		byte[] byt = null;
		ByteArrayOutputStream out = null;
		Document doc = null;
		try {
			String xml = parseXMLString(client);
			doc = printingService.getDomSourceForXML(xml);
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (JAXBException | ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError | TransformerException e1) {
			logger.error(e1.getMessage(), e1);
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
	public ResponseEntity<byte[]> printDonarReport(@PathVariable("donarId") Integer donarId, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, HttpServletRequest request) throws ParseException {
		String message;
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
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.OK);
		}

		//check date was not empty
		if(startDate == "" || endDate == "") {
			message = "Please check date filed. Something is missing in date input field";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.OK);
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
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.OK);
		}

		Date firstDate = DATE_FORMATTER.parse(startDate);
		Date lastDate = DATE_FORMATTER.parse(endDate);

		if(firstDate.compareTo(lastDate) > 0) {
			firstDate = DATE_FORMATTER.parse(endDate);
			lastDate = DATE_FORMATTER.parse(startDate);
		}

		List<Donation> donations = donationService.findAllDonationByClientIdAndPayDateBetweenStartDateToEndDate(client.getClientId(), donarId == -1 ? null : donarId, firstDate, lastDate);
		if(donations.isEmpty()) {
			message = "No data found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.OK);
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
		double totalPayableAmount = 0;
		double totalPaid = 0;
		double totalDue = 0;
		for(Donation donation : donations) {
			totalPayableAmount = totalPayableAmount + donation.getPayableAmount();
			totalPaid = totalPaid + donation.getPaid();
			totalDue = totalDue + donation.getDue();
		}
		client.setTotalPayableAmount(totalPayableAmount);
		client.setTotalPaid(totalPaid);
		client.setTotalDue(totalDue);
		client.setSiteLogo("site_logo.png");
		client.setReportName("Donars Transactions Info");

		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(siteConfig != null && siteConfig.getLogo() != null && Boolean.TRUE == siteConfig.isEnableLogo()) {
			client.setSiteLogo(siteConfig.getLogo());
		}

		byte[] byt = null;
		ByteArrayOutputStream out = null;
		Document doc = null;
		try {
			String xml = parseXMLString(client);
			doc = printingService.getDomSourceForXML(xml);
			out = printingService.transfromToPDFBytes(doc, template.toString(), request);
		} catch (JAXBException | ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError | TransformerException e1) {
			logger.error(e1.getMessage(), e1);
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

	private String parseXMLString(Dealer dealer) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Dealer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		jaxbMarshaller.marshal(dealer, result);
		return result.toString();
	}

	private String parseXMLString(Client client) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Client.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		jaxbMarshaller.marshal(client, result);
		return result.toString();
	}
}
