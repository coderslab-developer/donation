package com.sil.donation.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Authorities;
import com.sil.donation.entity.Client;
import com.sil.donation.entity.ClientServiceUpdateInfo;
import com.sil.donation.entity.Donar;
import com.sil.donation.entity.Users;
import com.sil.donation.exception.SilException;
import com.sil.donation.model.ClientDashboard;
import com.sil.donation.service.AuthoritiesService;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.ClientServiceUpdateInfoService;
import com.sil.donation.service.DealerService;
import com.sil.donation.service.DonarService;
import com.sil.donation.service.UsersService;
import com.sil.donation.util.ImageResizer;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/client")
@PropertySource("classpath:sil.properties")
public class ClientController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	private static final String PAGE_TITLE = "Add client";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "client";
	private static final String LOCATION_TO = "add_client";
	private static final String LOCATION = "views/client/";
	private static final String CLIENT_PHOTO_DIR = "resources/upload/client/";

	@Autowired private ClientService clientService;
	@Autowired private DealerService dealerService;
	@Autowired private UsersService usersService;
	@Autowired private DonarService donarService;
	@Autowired private AuthoritiesService authoritiesService;
	@Autowired private CategoryService categoryService;
	@Autowired private Environment environment;
	@Autowired private ClientServiceUpdateInfoService clientServiceUpdateInfoService;

	@RequestMapping
	public String loadClientPage(Model model) {
		model.addAttribute("client", new Client());
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addClient(@ModelAttribute("client") @Valid Client client, BindingResult result, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
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
				String uploadPath = request.getServletContext().getRealPath(CLIENT_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("client.photo.size.width");
				String height = environment.getProperty("client.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				client.setPhoto(fileName);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		//without image part
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.YEAR, 1);
		Date nextYear = calendar.getTime();
		client.setRegisterDate(today);
		client.setExpireDate(nextYear);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		try {
			client.setDealerId(dealerService.findByUsernameAndArchive(username, false).getDealerId());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		try {
			clientService.save(client);
			usersService.createUsersFromClient(client);
			authoritiesService.createAuthorityForClient(client);
			redirect.addFlashAttribute("sm", "You are successfully add client info into your system");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Client info not saved successfully");
			logger.error(e.getMessage(), e);
		}
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateClientInfo(@ModelAttribute("client") Client client,  @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		if(!client.getPassword().isEmpty() && client.getPassword().length() < 6) {
			redirect.addFlashAttribute("em", "Password must be greater then 6 character");
			return REDIRECT + "client/edit/" + client.getClientId();
		}

		Client c = null;
		Users users = null;
		Authorities authorities = null;
		try {
			c = clientService.findByClientIdAndArchive(client.getClientId(), false);
			users = usersService.findByUsernameAndArchive(c.getUsername(), false);
			authorities = authoritiesService.findByUsernameAndArchive(c.getUsername(), false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(!client.getClientName().isEmpty()) {
			c.setClientName(client.getClientName());
		}
		if(!client.getEmail().isEmpty()) {
			c.setEmail(client.getEmail());
			users.setEmail(c.getEmail());
		}
		if(!client.getPassword().isEmpty()) {
			c.setPassword(client.getPassword());
			users.setPassword(c.getPassword());
		}
		if(!client.getMobile().isEmpty()) {
			c.setMobile(client.getMobile());
		}
		if(!client.getAddress().isEmpty()) {
			c.setAddress(client.getAddress());
		}
		c.setSmsService(client.isSmsService());

		//Remove previous image
		if(!file.isEmpty() && client.getClientId() != null && c.getPhoto() != null) {
			String imageName = c.getPhoto();
			String uploadPath = request.getServletContext().getRealPath(CLIENT_PHOTO_DIR);
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
			if(logger.isDebugEnabled()) logger.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = request.getServletContext().getRealPath(CLIENT_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("client.photo.size.width");
				String height = environment.getProperty("client.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				c.setPhoto(fileName);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		//without image part
		users.setUsername(c.getUsername());
		authorities.setUsername(c.getUsername());
		try {
			clientService.save(c);
			usersService.save(users);
			authoritiesService.save(authorities);
			redirect.addFlashAttribute("sm", "Client info update successfully");
		} catch (Exception e) {
			redirect.addFlashAttribute("em", "Client info not update");
			logger.error(e.getMessage(), e);
		}
		
		return REDIRECT + "client/edit/" + client.getClientId();
	}

	@RequestMapping(value = "/edit/{clientId}")
	public String editClient(@PathVariable("clientId") Integer clientId, Model model) {
		model.addAttribute("pageTitle", "Edit Client");
		try {
			model.addAttribute("client", clientService.findByClientIdAndArchive(clientId, false));
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		return LOCATION + "edit_client";
	}

	@RequestMapping(value = "/view/{clientId}")
	public String viewClientInfo(@PathVariable("clientId") int clientId, Model model) {
		try {
			Client client = clientService.findByClientIdAndArchive(clientId, false);
			List<Donar> donars = donarService.findAllByClientIdAndArchive(clientId, false);
			donars.stream().forEach(d -> {
				try {
					d.setCategoryName(categoryService.findByCategoryIdAndArchive(d.getCategoryId(), false).getName());
				} catch (SilException e) {
					logger.error(e.getMessage(), e);
				}
			});
			client.setDonars(donars);
			client.setDealerName(dealerService.findByDealerIdAndArchive(client.getDealerId(), false).getDealerName());
			model.addAttribute("client", client);
			model.addAttribute("clientDashboard", getClientDashboardInfo(clientId));
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		return LOCATION + "view_client_profile";
	}

	@RequestMapping(value = "/changeStatus/{clientId}")
	public String changeStatus(@PathVariable("clientId") int clientId, RedirectAttributes redirect) {
		try {
			Client client = clientService.findByClientIdAndArchive(clientId, false);
			client.setStatus(!client.isStatus());
			
			Users users = usersService.findByUsernameAndArchive(client.getUsername(), false);
			users.setEnabled(!users.isEnabled());
			
			clientService.save(client);
			usersService.save(users);
			
			redirect.addFlashAttribute("sm", "Client status chage successfully");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return REDIRECT;
	}

	@RequestMapping(value = "/archive/{clientId}")
	public String archiveClient(@PathVariable("clientId") int clientId, RedirectAttributes redirect) {
		try {
			Client client = clientService.findByClientIdAndArchive(clientId, false);
			client.setArchive(true);
			
			Users users = usersService.findByUsernameAndArchive(client.getUsername(), false);
			users.setArchive(true);
			users.setEnabled(false);

			Authorities authorities = authoritiesService.findByUsernameAndArchive(client.getUsername(), false);
			authorities.setArchive(true);
			
			List<Donar> donars = donarService.findAllByClientId(client.getClientId());
			for(Donar d : donars) {
				d.setArchive(true);
				donarService.save(d);
			}
			clientService.save(client);
			usersService.save(users);
			authoritiesService.save(authorities);
			
			redirect.addFlashAttribute("sm", "Client deleted successfully");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return REDIRECT;
	}

	public ClientDashboard getClientDashboardInfo(int clientId) throws SilException {
		String username = clientService.findByClientIdAndArchive(clientId, false).getUsername();
		ClientDashboard clientDashboard = new ClientDashboard();
		List<Donar> donars = donarService.findAllByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false);
		clientDashboard.setTotalDonar(donars.size());
		clientDashboard.setActiveDonar(donars.stream().filter(d -> d.isStatus() == Boolean.TRUE).collect(Collectors.toList()).size());
		clientDashboard.setInactiveDonar(donars.stream().filter(d -> d.isStatus() == Boolean.FALSE).collect(Collectors.toList()).size());
		clientDashboard.setNumberOfPayeeDonarInThisMonth(0);
		return clientDashboard;
	}

	@RequestMapping(value = "/updateService/{clientId}", method = RequestMethod.GET)
	public String updateClientService(@PathVariable("clientId") Integer clientId, Model model, RedirectAttributes redirect) {
		Client client = null;
		try {
			client = clientService.findByClientIdAndArchive(clientId, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(client == null) {
			redirect.addFlashAttribute("em", "No client found");
			return REDIRECT;
		}

		ClientServiceUpdateInfo csui = new ClientServiceUpdateInfo();
		csui.setClientId(client.getClientId());
		csui.setPreviousExpireDate(client.getExpireDate());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Date nextExpireDate = calendar.getTime();
		csui.setNextExpireDate(nextExpireDate);
		csui.setClientName(client.getClientName());

		model.addAttribute("csui", csui);
		model.addAttribute("pageTitle", "Update Service");
		return LOCATION + "update_service";
	}

	@RequestMapping(value = "/updateService", method = RequestMethod.POST)
	public String updateService(Integer clientId, RedirectAttributes redirect) {
		Client client = null;
		try {
			client = clientService.findByClientIdAndArchive(clientId, false);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}
		if(client == null) {
			redirect.addFlashAttribute("em", "No client found");
			return REDIRECT;
		}

		ClientServiceUpdateInfo csui = new ClientServiceUpdateInfo();
		csui.setClientId(client.getClientId());
		csui.setPreviousExpireDate(client.getExpireDate());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Date nextExpireDate = calendar.getTime();
		csui.setNextExpireDate(nextExpireDate);
		csui.setClientName(client.getClientName());
		csui.setUpdateBy(client.getUsername());
		csui.setUpdateDate(new Date());

		boolean status = clientServiceUpdateInfoService.save(csui);
		Users users = null;
		if(Boolean.TRUE == status) {
			try {
				users = usersService.findByUsernameAndArchive(client.getUsername(), false);
				users.setEnabled(true);
			} catch (SilException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if(users == null) {
			redirect.addFlashAttribute("em", "Cant found userinfo for this client");
			return REDIRECT;
		}

		client.setExpireDate(csui.getNextExpireDate());
		client.setStatus(true);

		try {
			clientService.save(client);
			usersService.save(users);
			clientServiceUpdateInfoService.save(csui);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		redirect.addFlashAttribute("sm", "Client Service update to " + client.getExpireDate() + " successfully");
		return REDIRECT + "client/updateService/" + clientId;
	}

}
