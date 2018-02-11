package com.sil.donation.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;
import com.sil.donation.service.DonarService;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(DonarController.class);

	@Autowired private DonarService donarService;
	@Autowired private CategoryService categoryService;
	@Autowired private ClientService clientService;

	@RequestMapping
	public String donar(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		model.addAttribute("pageTitle", PAGE_TITLE);
		Donar donar = new Donar();
		try {
			donar.setCategories(categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
		} catch (SilException e) {
			e.printStackTrace();
		}
		model.addAttribute("donar", donar);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createDonar(@ModelAttribute("donar") @Valid Donar donar, BindingResult result, RedirectAttributes redirect, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(result.hasErrors()) {
			model.addAttribute("pageTitle", PAGE_TITLE);
			try {
				donar.setCategories(categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
			} catch (SilException e) {
				e.printStackTrace();
			}
			return LOCATION + LOCATION_TO;
		}
		try {
			donar.setClientId(clientService.findByUsernameAndArchive(username, false).getClientId());
		} catch (SilException e) {
			e.printStackTrace();
		}
		donar.setRegisterDate(new Date());
		donar.setArchive(false);
		donarService.save(donar);
		redirect.addFlashAttribute("sm", "Donar created successfully");
		return REDIRECT + REDIRECT_TO;
	}
	
	@RequestMapping(value = "/changeStatus/{donarId}")
	public String changeStatus(@PathVariable("donarId") int donarId, RedirectAttributes redirect) {
		try {
			Donar donar = donarService.findByDonarId(donarId, false);
			donar.setStatus(!donar.isStatus());
			donarService.save(donar);
			redirect.addFlashAttribute("sm", "Donar status change successfully");
		} catch (SilException e) {
			e.printStackTrace();
		}
		return REDIRECT;
	}
	
	@RequestMapping(value = "/view/{donarId}")
	public String viewProfile(@PathVariable("donarId") int donarId, Model model) {
		try {
			Donar donar = donarService.findByDonarId(donarId, false);
			donar.setCategoryName(categoryService.findByCategoryIdAndArchive(donar.getCategoryId(), false).getName());
			model.addAttribute("donar", donar);
		} catch (SilException e) {
			e.printStackTrace();
		}
		return LOCATION + "view_donar_profile";
	}

}
