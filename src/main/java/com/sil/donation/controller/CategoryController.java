package com.sil.donation.controller;

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

import com.sil.donation.entity.Category;
import com.sil.donation.exception.SilException;
import com.sil.donation.service.CategoryService;
import com.sil.donation.service.ClientService;

/**
 * @author Zubayer Ahamed
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	private static final String PAGE_TITLE = "Add category";
	private static final String REDIRECT = "redirect:/";
	private static final String REDIRECT_TO = "category";
	private static final String LOCATION_TO = "add_category";
	private static final String LOCATION = "views/category/";

	@Autowired private CategoryService categoryService;
	@Autowired private ClientService clientService;

	@RequestMapping
	public String category(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		model.addAttribute("category", new Category());
		try {
			model.addAttribute("categories", categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		model.addAttribute("pageTitle", PAGE_TITLE);
		return LOCATION + LOCATION_TO;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createCategory(@ModelAttribute("category") @Valid Category category, BindingResult result, Model model, RedirectAttributes redirect) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (result.hasErrors()) {
			try {
				model.addAttribute("categories", categoryService.findByClientIdAndArchive(clientService.findByUsernameAndArchive(username, false).getClientId(), false));
			} catch (SilException e) {
				logger.error(e.getMessage());
			}
			return LOCATION + LOCATION_TO;
		}
		try {
			category.setClientId(clientService.findByUsernameAndArchive(username, false).getClientId());
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		category.setArchive(false);
		categoryService.save(category);
		redirect.addFlashAttribute("sm", "Category create successfully");
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/changeStatus/{categoryId}", method = RequestMethod.GET)
	public String changeCategoryStatus(@PathVariable("categoryId") Integer categoryId, RedirectAttributes redirect) {
		Category category = null;
		try {
			category = categoryService.findByCategoryIdAndArchive(categoryId, false);
			category.setStatus(!category.isStatus());
			categoryService.save(category);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		redirect.addFlashAttribute("sm", "Status changed successfully");
		return REDIRECT + REDIRECT_TO;
	}

	@RequestMapping(value = "/archive/{categoryId}", method = RequestMethod.GET)
	public String archieveCategory(@PathVariable("categoryId") Integer categoryId, RedirectAttributes redirect) {
		Category category = null;
		try {
			category = categoryService.findByCategoryIdAndArchive(categoryId, false);
			category.setArchive(true);
			categoryService.save(category);
		} catch (SilException e) {
			logger.error(e.getMessage());
		}
		redirect.addFlashAttribute("sm", "Category deleted successfully");
		return REDIRECT + REDIRECT_TO;
	}
}
