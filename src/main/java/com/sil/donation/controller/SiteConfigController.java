/**
 * 
 */
package com.sil.donation.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.SiteConfig;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.SiteConfigService;
import com.sil.donation.util.ImageResizer;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/siteConfig")
public class SiteConfigController {

	private static final Logger logger = LoggerFactory.getLogger(SiteConfigController.class);

	private static final String PAGE_TITLE = "Site Config";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "siteConfig";
	private static final String LOCATION_TO = "site_config";
	private static final String LOCATION = "views/site/";
	private static final String SITE_LOGO_DIR = "resources/upload/site/logo/";

	@Autowired SiteConfigService siteConfigService;
	@Autowired Environment environment;

	@GetMapping("/{username}")
	public String loadSiteConfigPage(@PathVariable("username") String username, Model model) {
		model.addAttribute("pageTitle", PAGE_TITLE);
		SiteConfig siteConfig = null;
		try {
			siteConfig = siteConfigService.findByUsername(username);
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(siteConfig == null) {
			siteConfig = new SiteConfig();
			siteConfig.setUsername(username);
			siteConfig.setEnableLogo(true);
		}

		model.addAttribute("siteConfig", siteConfig);
		return LOCATION + LOCATION_TO;
	}

	@PostMapping
	public String updateSiteConfig(SiteConfig siteConfig, @RequestParam("file") MultipartFile file, HttpServletRequest request, RedirectAttributes redirect, HttpSession session) {
		logger.info("siteconfig values {}, file {}", siteConfig, file);
		SiteConfig sc = null;
		try {
			sc = siteConfigService.findByUsername(siteConfig.getUsername());
		} catch (SilException e) {
			logger.error(e.getMessage(), e);
		}

		if(file.isEmpty() && sc != null) {
			siteConfig.setLogo(sc.getLogo());
		}

		if(!file.isEmpty()) {
			//Remove previous logo
			if(sc != null && sc.getLogo() != null) {
				String imageName = sc.getLogo();
				String uploadPath = request.getServletContext().getRealPath(SITE_LOGO_DIR);
				File image = new File(uploadPath +  imageName);
				if(image.exists()) {
					if(!image.delete()) {
						image.delete();
					}
				}
			}

			//Rename the file 
			String extension = null;
			int j = file.getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = file.getOriginalFilename().substring(j + 1);
			}
			String fileName = siteConfig.getUsername() + "." + extension;
			if(logger.isDebugEnabled()) logger.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = request.getServletContext().getRealPath(SITE_LOGO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				//Upload file into server
				Files.copy(file.getInputStream(), Paths.get(uploadPath, fileName));

				//resize the uploaded image
				String width = environment.getProperty("site.logo.size.width");
				String height = environment.getProperty("site.logo.size.height");
				ImageResizer.resize(dir + "\\" + fileName, dir + "\\" + fileName, Integer.valueOf(width), Integer.valueOf(height));

				//set photo name into database
				siteConfig.setLogo(fileName);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		boolean status = false;
		if(sc != null) {
			//merge with previous data
			sc.setLogo(siteConfig.getLogo());
			sc.setSiteName(siteConfig.getSiteName());
			sc.setEnableLogo(siteConfig.isEnableLogo());
			status = siteConfigService.save(sc);
			session.setAttribute("siteConfig", sc);
		}else {
			//make a new data
			status = siteConfigService.save(siteConfig);
			session.setAttribute("siteConfig", siteConfig);
		}

		if(Boolean.TRUE == status) {
			redirect.addFlashAttribute("sm", "Site info update successfully");
		}else {
			redirect.addFlashAttribute("em", "Site info not update");
		}

		return REDIRECT + REDIRECT_TO + '/' + siteConfig.getUsername(); 
	}
}
