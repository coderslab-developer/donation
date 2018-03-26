package com.sil.donation.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.sil.donation.entity.Admin;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.PrintingService;
import com.sil.donation.service.SiteConfigService;
import com.sil.donation.util.ClientDocumentGenerator;
import com.sil.donation.util.DealerDocumentGenerator;
import com.sil.donation.util.DonarsTransactionDocumentGenerator;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class PrintingServiceImpl implements PrintingService {

	private static final Logger logger = LoggerFactory.getLogger(PrintingServiceImpl.class);

	@Autowired private ClientService clientService;
	@Autowired private DealerDocumentGenerator dealerDocumentGenerator; 
	@Autowired private ClientDocumentGenerator clientDocumentGenerator;
	@Autowired private SiteConfigService siteConfigService;
	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;
	@Autowired private DealerService dealerService;
	@Autowired private DonarsTransactionDocumentGenerator donarsTransactionDocumentGenerator;

	@Override
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template, HttpServletRequest request)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(template);

		Source xslSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
		if (transformer == null) {
			throw new TransformerException("File not found: " + template);
		}

		//for image path setting
		String serverPath = request.getSession().getServletContext().getRealPath("/");

		FopFactory fopFactory = FopFactory.newInstance(new File(serverPath).toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(new DOMSource(doc), res);
		return out;
	}

	@Override
	public Document generateDealerProfileDocument(Dealer dealer) throws ParserConfigurationException {
		List<Client> clients = new ArrayList<>();
		try {
			clients = clientService.findAllByDealerId(dealer.getDealerId());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return dealerDocumentGenerator.dealerProfileDocument(dealer, siteConfig);
	}

	@Override
	public Document generateClientProfileDocument(Client client) throws ParserConfigurationException {
		try {
			client.setDealerName(dealerService.findByDealerIdAndArchive(client.getDealerId(), false).getDealerName());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		//get donars
		List<Donar> donars = null;
		try {
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
			client.setActiveDonar(donars.stream().filter(d -> Boolean.TRUE == d.isStatus() && Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());
			client.setInactiveDonar(donars.stream().filter(d -> Boolean.FALSE == d.isStatus() && Boolean.FALSE == d.isArchive()).collect(Collectors.toList()).size());
			client.setNumberOfPayeeDonarInThisMonth(0);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return clientDocumentGenerator.clientProfileDocument(client, siteConfig);
	}

	@Override
	public Document generateDonarProfileDocument(Donar donar) throws ParserConfigurationException {
		
		return null;
	}

	@Override
	public Document generateAdminProfileDocument(Admin admin) throws ParserConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document generateDonarsDonationTransactionsReport(Client client) throws ParserConfigurationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		return donarsTransactionDocumentGenerator.generateDonarsTransactionReport(client, siteConfig);
	}

}
