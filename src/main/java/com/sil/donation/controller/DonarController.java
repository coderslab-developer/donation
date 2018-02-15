package com.sil.donation.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DonarService;
import com.sil.donation.util.ImageResizer;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/donar")
public class DonarController {

	private static final String PAGE_TITLE = "Add donar";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "donar";
	private static final String LOCATION_TO = "add_donar";
	private static final String LOCATION = "views/donar/";
	private static final String DONAR_PHOTO_DIR = "resources/upload/donar/";
	private static final Logger LOGGER = LoggerFactory.getLogger(DonarController.class);

	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;
	@Autowired private ClientService clientService;
	@Autowired private Environment environment;

	@RequestMapping
	public String donar(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		model.addAttribute("pageTitle", PAGE_TITLE);
		Donar donar = new Donar();
		try {
			donar.setCategories(categoryService.findByClientIdAndStatusAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), true, false));
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		model.addAttribute("donar", donar);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createDonar(@ModelAttribute("donar") @Valid Donar donar, BindingResult result, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(result.hasErrors()) {
			model.addAttribute("pageTitle", PAGE_TITLE);
			try {
				donar.setCategories(categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
			} catch (SilException e) {
				LOGGER.error("Error : {}" , e.getMessage());
			}
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
				String uploadPath = request.getServletContext().getRealPath(DONAR_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("donar.photo.size.width");
				String height = environment.getProperty("donar.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				donar.setPhoto(fileName);
			} catch (IOException e) {
				LOGGER.error("Error: {}" , e.getMessage());
			}
		}

		//without image part
		try {
			donar.setClientId(clientService.findByUsernameAndArchive(username, false).getClientId());
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		donar.setRegisterDate(new Date());
		donar.setArchive(false);
		donarService.save(donar);
		redirect.addFlashAttribute("sm", "Donar created successfully");
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateDonar(@ModelAttribute("donar") @Valid Donar donar, BindingResult result, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirect, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(result.hasErrors()) {
			model.addAttribute("pageTitle", "Edit Donar");
			try {
				donar.setCategories(categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
			} catch (SilException e) {
				LOGGER.error("Error : {}" , e.getMessage());
			}
			return LOCATION + "edit_donar";
		}

		Donar d = null;
		if(donar.getDonarId() != null) {
			try {
				d = donarService.findByDonarId(donar.getDonarId(), false);
			} catch (SilException e) {
				LOGGER.error("Error : {}" , e.getMessage());
			}
		}

		//Remove previous image
		if(!file.isEmpty() && donar.getDonarId() != null && d.getPhoto() != null) {
			String imageName = d.getPhoto();
			String uploadPath = request.getServletContext().getRealPath(DONAR_PHOTO_DIR);
			File image = new File(uploadPath +  imageName);
			if(image.exists()) {
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
				String uploadPath = request.getServletContext().getRealPath(DONAR_PHOTO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("donar.photo.size.width");
				String height = environment.getProperty("donar.photo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				d.setPhoto(fileName);
			} catch (IOException e) {
				LOGGER.error("Error: {}" , e.getMessage());
			}
		}

		//without image part
		d.setDonarName(donar.getDonarName());
		d.setCategoryId(donar.getCategoryId());
		d.setEmail(donar.getEmail());
		d.setAddress(donar.getAddress());
		d.setMobile(donar.getMobile());
		d.setPayableAmount(donar.getPayableAmount());
		d.setInstituteName(donar.getInstituteName());
		d.setSmsService(donar.isSmsService());
		donarService.save(d);
		redirect.addFlashAttribute("sm", "Donar Info Update successfully");
		return REDIRECT + "donar/edit/" + d.getDonarId();
	}

	@RequestMapping("/edit/{donarId}")
	public String editDonar(@PathVariable("donarId") Integer donarId, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Donar donar = null;
		try {
			donar = donarService.findByDonarId(donarId, false);
			donar.setCategories(categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		model.addAttribute("pageTitle", "Edit Donar");
		model.addAttribute("donar", donar);
		return LOCATION + "edit_donar";
	}

	@RequestMapping(value = "/changeStatus/{donarId}")
	public String changeStatus(@PathVariable("donarId") Integer donarId, RedirectAttributes redirect) {
		try {
			Donar donar = donarService.findByDonarId(donarId, false);
			donar.setStatus(!donar.isStatus());
			donarService.save(donar);
			redirect.addFlashAttribute("sm", "Donar status change successfully");
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		return REDIRECT;
	}

	@RequestMapping(value = "/view/{donarId}")
	public String viewProfile(@PathVariable("donarId") Integer donarId, Model model) {
		try {
			Donar donar = donarService.findByDonarId(donarId, false);
			donar.setCategoryName(categoryService.findByCategoryIdAndArchive(donar.getCategoryId(), false).getName());
			model.addAttribute("donar", donar);
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		return LOCATION + "view_donar_profile";
	}

	@RequestMapping(value = "/archive/{donarId}")
	public String archiveDonar(@PathVariable("donarId") Integer donarId, RedirectAttributes redirect) {
		try {
			Donar donar = donarService.findByDonarId(donarId, false);
			donar.setArchive(!donar.isArchive());
			donarService.save(donar);
			redirect.addFlashAttribute("sm", "Donar deleted successfully");
		} catch (SilException e) {
			LOGGER.error("Error : {}" , e.getMessage());
		}
		return REDIRECT;
	}

}
