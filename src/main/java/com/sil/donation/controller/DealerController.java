package com.sil.donation.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.Dealer;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.AdminService;
import com.sil.donation.service.AuthoritiesService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.UsersService;
import com.sil.donation.util.ImageResizer;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/dealer")
@PropertySource("classpath:sil.properties")
public class DealerController {

	private static final Logger logger = LoggerFactory.getLogger(DealerController.class);

	private static final String PAGE_TITLE = "Add dealer";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "dealer";
	private static final String LOCATION_TO = "add_dealer";
	private static final String LOCATION = "views/dealer/";
	private static final String DEALER_PHOTO_DIR = "resources/upload/dealer/";

	@Autowired private DealerService dealerService;
	@Autowired private UsersService usersService;
	@Autowired private AuthoritiesService authoritiesService;
	@Autowired private ClientService clientService;
	@Autowired private DonarService donarService;
	@Autowired private Environment environment;
	@Autowired private AdminService adminService;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping
	public String loadDealerPage(Model model) {
		model.addAttribute("dealer", new Dealer());
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addDealer(@ModelAttribute("dealer") @Valid Dealer dealer, BindingResult result, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		if(logger.isDebugEnabled()) logger.debug("Dealer values {}" , dealer);

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
			if(logger.isDebugEnabled()) logger.debug("File name is now: {}", fileName);

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
				logger.error(e.getMessage());
			}
		}

		//without image part
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		dealer.setRegisterDate(new Date());
		try {
			dealer.setAdminId(adminService.findByUsername(username).getAdminId());
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		dealer.setPassword(bCryptPasswordEncoder.encode(dealer.getPassword()));
		try {
			dealerService.save(dealer);
			usersService.createUsersFromDealer(dealer);
			authoritiesService.createAuthorityForDealer(dealer);
			redirect.addFlashAttribute("sm", "A new dealer add successfully into your system");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Dealer info not saved");
			logger.error(e.getMessage());
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
			logger.error(e1.getMessage());
		}

		if(!dealer.getDealerName().isEmpty()) {
			d.setDealerName(dealer.getDealerName());
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
			d.setPassword(bCryptPasswordEncoder.encode(dealer.getPassword()));
			users.setPassword(d.getPassword());
		}

		//Remove previous image
		if(!file.isEmpty() && dealer.getDealerId() != null && d.getPhoto() != null) {
			String imageName = d.getPhoto();
			String uploadPath = request.getServletContext().getRealPath(DEALER_PHOTO_DIR);
			File image = new File(uploadPath +  imageName);
			Path path = Paths.get(image.getAbsolutePath());
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				logger.error("File not delete: " + e.getMessage(), e);
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
			if(logger.isDebugEnabled()) logger.debug("File name is now: {}", fileName);

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
				logger.error(e.getMessage());
			}
		}

		//without image part
		users.setUsername(d.getUsername());
		authorities.setUsername(d.getUsername());
		try {
			dealerService.save(d);
			usersService.save(users);
			authoritiesService.save(authorities);
			redirect.addFlashAttribute("sm", "Dealer info update successfully");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Dealer info not saved");
			logger.error(e.getMessage());
		}

		return REDIRECT + "dealer/edit/" + dealer.getDealerId();
	}

	@RequestMapping(value = "/edit/{dealerId}")
	public String editDealer(@PathVariable("dealerId") Integer dealerId, Model model) {
		try {
			model.addAttribute("pageTitle", PAGE_TITLE);
			model.addAttribute("dealer", dealerService.findByDealerIdAndArchive(dealerId, false));
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		return LOCATION + "edit_dealer";
	}

	@RequestMapping(value = "/view/{dealerId}", method = RequestMethod.GET)
	public String viewDealerInfo(@PathVariable("dealerId") int dealerId, Model model) {
		Dealer dealer = getDealerDashboardInfo(dealerId);
		model.addAttribute("dealer", dealer);
		model.addAttribute("dealerDashboard", dealer);
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
		}
		return REDIRECT;
	}

	@GetMapping("/photoRemove/{dealerId}")
	public String removeDealerPhoto(@PathVariable("dealerId") Integer dealerId, RedirectAttributes redirect, HttpServletRequest request) {
		Dealer dealer = null;
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(dealer == null) {
			redirect.addFlashAttribute("em", "No dealer found for remove photo");
			return REDIRECT + REDIRECT_TO + "/view/" + dealerId;
		}

		//Remove previous image
		String imageName = dealer.getPhoto();
		String uploadPath = request.getServletContext().getRealPath(DEALER_PHOTO_DIR);
		File image = new File(uploadPath +  imageName);
		Path path = Paths.get(image.getAbsolutePath());
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.error("File not delete: " + e.getMessage(), e);
		}

		dealer.setPhoto("");
		try {
			dealerService.save(dealer);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		redirect.addFlashAttribute("sm", "Photo remove successfully");
		return REDIRECT + REDIRECT_TO + "/view/" + dealerId;
	}

	public Dealer getDealerDashboardInfo(int dealerId) {
		Dealer dealer =new Dealer();
		List<Client> clients = new ArrayList<>();
		try {
			dealer = dealerService.findByDealerIdAndArchive(dealerId, false);
			clients = clientService.findAllByDealerId(dealer.getDealerId());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		dealer.setClients(clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()));
		dealer.setActiveClients(clients.stream().filter(c -> Boolean.TRUE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		dealer.setInactiveClients(clients.stream().filter(c -> Boolean.FALSE == c.isStatus()).filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		dealer.setTotalSellOfSoftware(clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList()).size());
		List<Client> renewalClients = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		for(Client c : clients.stream().filter(c -> Boolean.FALSE == c.isArchive()).collect(Collectors.toList())) {
			cal1.setTime(c.getExpireDate());
			cal2.setTime(new Date());
			if((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
				renewalClients.add(c);
			}
		}
		dealer.setServiceRenewOnThisMonth(renewalClients.size());
		return dealer;
	}

}
