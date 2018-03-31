package com.sil.donation.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sil.donation.entity.Client;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.PrintingService;
import com.sil.donation.service.SiteConfigService;
import com.sil.donation.util.ClientDocumentGenerator;
import com.sil.donation.util.DonarsTransactionDocumentGenerator;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class PrintingServiceImpl implements PrintingService {

	private static final Logger logger = LoggerFactory.getLogger(PrintingServiceImpl.class);

	@Autowired private ClientDocumentGenerator clientDocumentGenerator;
	@Autowired private SiteConfigService siteConfigService;
	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;
	@Autowired private DealerService dealerService;
	@Autowired private DonarsTransactionDocumentGenerator donarsTransactionDocumentGenerator;

	@Override
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template, HttpServletRequest request) throws TransformerFactoryConfigurationError, TransformerException, FOPException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(template);

		Source xslSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
		if (transformer == null) {
			throw new TransformerException("Template File not found: " + template);
		}

		//for image path setting
		String serverPath = request.getSession().getServletContext().getRealPath("/");

		FopFactory fopFactory = FopFactory.newInstance(new File(serverPath).toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		// Make sure the XSL transformation's result is piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());
		// Start the transformation and rendering process
		transformer.transform(new DOMSource(doc), res);
		return out;
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

	@Override
	public ByteArrayOutputStream transfromToThermalBytes(Document document, String template) throws TransformerException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Setup a buffer to obtain the content length
		Source xsltSrc = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(template));
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSrc);
		// Start the transformation and rendering process
		transformer.transform(new DOMSource(document), new StreamResult(out));
		return out;
	}

	@Override
	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource(new StringReader(xml));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}

}
