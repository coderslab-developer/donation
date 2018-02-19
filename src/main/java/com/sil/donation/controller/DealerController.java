package com.sil.donation.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.DealerDashboard;
import com.sil.donation.service.AuthoritiesService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.PrintingService;
import com.sil.donation.service.UsersService;
import com.sil.donation.util.ImageResizer;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/dealer")
public class DealerController {

	private static final String PAGE_TITLE = "Add dealer";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "dealer";
	private static final String LOCATION_TO = "add_dealer";
	private static final String LOCATION = "views/dealer/";
	private static final String DEALER_PHOTO_DIR = "resources/upload/dealer/";
	private static final Logger LOGGER = LoggerFactory.getLogger(DealerController.class);
	private static final String XSL_DIR = "src//main//resources//static//xsl//";

	@Autowired private DealerService dealerService;
	@Autowired private UsersService usersService;
	@Autowired private AuthoritiesService authoritiesService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private Environment environment;
	@Autowired private PrintingService printingService;

	@RequestMapping
	public String loadDealerPage(Model model) {
		model.addAttribute("dealer", new Dealer());
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addDealer(@ModelAttribute("dealer") @Valid Dealer dealer, BindingResult result, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("Dealer values {}" , dealer);

		if (result.hasErrors()) {
			model.addAttribute("pageTitle", PAGE_TITLE);
			return LOCATION + LOCATION_TO;
		}

		//Image part
		if(!file.isEmpty()) {
			//Rename the file 
			String extension = null;
			int j = file.getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = file.getOriginalFilename().substring(j + 1);
			}
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			String fileName = UUID.randomUUID() +  timeStamp + "." + extension;
			if(LOGGER.isDebugEnabled()) LOGGER.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = request.getServletContext().getRealPath(DEALER_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("dealer.photo.size.width");
				String height = environment.getProperty("dealer.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				dealer.setPhoto(fileName);
			} catch (IOException e) {
				LOGGER.error("Error: {}" , e.getMessage());
			}
		}

		//without image part
		dealer.setRegisterDate(new Date());
		try {
			dealerService.save(dealer);
			usersService.createUsersFromDealer(dealer);
			authoritiesService.createAuthorityForDealer(dealer);
			redirect.addFlashAttribute("sm", "A new dealer add successfully into your system");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Dealer info not saved");
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public String updateDealerInfo(@ModelAttribute("dealer") Dealer dealer, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		if(!dealer.getPassword().isEmpty() && dealer.getPassword().length() < 6) {
			redirect.addFlashAttribute("em", "Password must be greater then 6 character");
			return REDIRECT + "dealer/edit/" + dealer.getDealerId();
		}
		
		Dealer d = null;
		Users users = null;
		Authorities authorities = null;
		try {
			d = dealerService.findByDealerIdAndArchive(dealer.getDealerId(), false);
			users = usersService.findByUsernameAndArchive(d.getUsername(), false);
			authorities = authoritiesService.findByUsernameAndArchive(d.getUsername(), false);
		} catch (SilException e1) {
			LOGGER.error("Error: {}", e1.getMessage());
		}
		
		if(!dealer.getDealerName().isEmpty()) {
			d.setDealerName(dealer.getDealerName());
		}
		if(!dealer.getUsername().isEmpty()) {
			d.setUsername(dealer.getUsername());
			users.setUsername(d.getUsername());
			authorities.setUsername(d.getUsername());
		}
		if(!dealer.getEmail().isEmpty()) {
			d.setEmail(dealer.getEmail());
			users.setEmail(d.getEmail());
		}
		if(!dealer.getMobile().isEmpty()) {
			d.setMobile(dealer.getMobile());
		}
		if(!dealer.getAddress().isEmpty()) {
			d.setAddress(dealer.getAddress());
		}
		if(!dealer.getPassword().isEmpty()) {
			d.setPassword(dealer.getPassword());
			users.setPassword(d.getPassword());
		}

		//Remove previous image
		if(!file.isEmpty() && dealer.getDealerId() != null && d.getPhoto() != null) {
			String imageName = d.getPhoto();
			String uploadPath = request.getServletContext().getRealPath(DEALER_PHOTO_DIR);
			System.out.println(uploadPath + imageName);
			File image = new File(uploadPath +  imageName);
			if(!image.delete()) {
				image.delete();
			}
		}

		//Image part
		if(!file.isEmpty()) {
			//Rename the file 
			String extension = null;
			int j = file.getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = file.getOriginalFilename().substring(j + 1);
			}
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			String fileName = UUID.randomUUID() +  timeStamp + "." + extension;
			if(LOGGER.isDebugEnabled()) LOGGER.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = request.getServletContext().getRealPath(DEALER_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("dealer.photo.size.width");
				String height = environment.getProperty("dealer.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				d.setPhoto(fileName);
			} catch (IOException e) {
				LOGGER.error("Error: {}" , e.getMessage());
			}
		}

		//without image part
		d.setUpdateDate(new Date());
		try {
			dealerService.save(d);
			usersService.save(users);
			authoritiesService.save(authorities);
			redirect.addFlashAttribute("sm", "Dealer info update successfully");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Dealer info not saved");
			LOGGER.error("Error: {}" , e.getMessage());
		}

		return REDIRECT + "dealer/edit/" + dealer.getDealerId();
	}
	
	@RequestMapping(value = "/edit/{dealerId}")
	public String editDealer(@PathVariable("dealerId") Integer dealerId, Model model) {
		try {
			model.addAttribute("pageTitle", PAGE_TITLE);
			model.addAttribute("dealer", dealerService.findByDealerIdAndArchive(dealerId, false));
		} catch (SilException e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return LOCATION + "edit_dealer";
	}

	@RequestMapping(value = "/view/{dealerId}", method = RequestMethod.GET)
	public String viewDealerInfo(@PathVariable("dealerId") int dealerId, Model model) {
		model.addAttribute("dealer", getDealerFullInfo(dealerId));
		try {
			model.addAttribute("dealerDashboard", getDealerDashboardInfo(dealerId));
		} catch (SilException e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return LOCATION + "view_dealer_profile";
	}

	@RequestMapping(value = "/changeStatus/{dealerId}", method = RequestMethod.GET)
	public String changeDealerStatus(@PathVariable("dealerId") int dealerId, RedirectAttributes redirect) {
		Dealer dealer = null;
		Users users = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
			dealer.setStatus(!dealer.isStatus());
			
			users = usersService.findByUsernameAndArchive(dealer.getUsername(), false);
			users.setEnabled(!users.isEnabled());
			
			dealerService.save(dealer);
			usersService.save(users);
			
			redirect.addFlashAttribute("sm", "Dealer status change successfully");
		} catch (Exception e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return REDIRECT;
	}

	@RequestMapping(value = "/archive/{dealerId}")
	public String archiveDealer(@PathVariable("dealerId") int dealerId, RedirectAttributes redirect) {
		try {
			Dealer dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
			dealer.setArchive(true);

			Users users = usersService.findByUsernameAndArchive(dealer.getUsername(), false);
			users.setArchive(true);
			users.setEnabled(false);

			Authorities authorities = authoritiesService.findByUsernameAndArchive(dealer.getUsername(), false);
			authorities.setArchive(true);

			dealerService.save(dealer);
			usersService.save(users);
			authoritiesService.save(authorities);
			List<Client> clients = clientService.findAllByDealerId(dealer.getDealerId());
			for(Client c : clients) {
				List<Donar> donars = donarService.findAllByClientId(c.getClientId());
				for(Donar d : donars) {
					d.setArchive(true);
					donarService.save(d);
				}
				Users u = usersService.findByUsernameAndArchive(c.getUsername(), false);
				u.setArchive(true);
				u.setEnabled(false);
				usersService.save(u);
				
				Authorities auth = authoritiesService.findByUsernameAndArchive(c.getUsername(), false);
				auth.setArchive(true);
				authoritiesService.save(auth);
				
				c.setArchive(true);
				clientService.save(c);
			}

			redirect.addFlashAttribute("sm", "Delaer deleted successfully");
		} catch (Exception e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return REDIRECT;
	}

	public Dealer getDealerFullInfo(int dealerId) {
		Dealer dealer = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
			dealer.setClients(clientService.findByDealerIdAndArchive(dealerId, false));
		} catch (Exception e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		return dealer;
	}

	public DealerDashboard getDealerDashboardInfo(int dealerId) throws SilException {
		Dealer dealer = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
		} catch (Exception e) {
			LOGGER.error("Error: {}" , e.getMessage());
		}
		DealerDashboard dealerDashboard = new DealerDashboard();
		dealerDashboard.setActiveClient(clientService.findByDealerIdAndStatusAndArchive(dealer.getDealerId(), true, false).size());
		dealerDashboard.setInactiveClient(clientService.findByDealerIdAndStatusAndArchive(dealer.getDealerId(), false, false).size());
		dealerDashboard.setTotalSellOfSoftware(clientService.findByDealerIdAndArchive(dealer.getDealerId(), false).size());
		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clientService.findByDealerIdAndArchive(dealer.getDealerId(), false)) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
				renewalClients.add(c);
			}
		}
		dealerDashboard.setServiceRenewOnThisMonth(renewalClients.size());
		return dealerDashboard;
	}
	
	@RequestMapping(value = "/print/{dealerId}")
	public ResponseEntity<byte[]> printDealerProfile(@PathVariable("dealerId") Integer dealerId, Model model, Locale locale) {
		String xslTemplate = "standard_dealer_profile_xsl_template.xsl";
		
		Dealer dealer = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
			return responseDocument(dealer, xslTemplate, locale);
		} catch (SilException e) {
			LOGGER.error(e.getMessage());
		}
		if(LOGGER.isDebugEnabled()) LOGGER.debug("Dealer : {}", dealer);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		String em = "Print create pdf error";
		
		return new ResponseEntity<>(em.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	private ResponseEntity<byte[]> responseDocument(Dealer dealer, String template, Locale locale){
		HttpHeaders headers = new HttpHeaders();
		
		ByteArrayOutputStream out = null;
		try {
			out = printingService.transformDealerToThermal(dealer, template);
			//headers.setContentType(new MediaType("application", "pdf"));
			//headers.setContentType(new MediaType("application", "csv"));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		String documentName = "dealer_" + Math.round((Math.random() * 10000)) + "" + (Calendar.getInstance().getTimeInMillis() % dealer.hashCode());
		String absolutePath = null;
		File tempFile = null;
		try {
			tempFile = File.createTempFile(documentName, ".pdf");
			FileOutputStream fout = new FileOutputStream(tempFile);
			//out.writeTo(fout);
			fout.write(out.toByteArray());
			fout.close();
			
			absolutePath = tempFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);
	}

}
