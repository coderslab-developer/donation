package com.sil.donation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Category;
import com.sil.donation.exception.SilException;
import com.sil.donation.repository.CategoryRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public List<Category> findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException {
		return categoryRepository.findByClientIdAndArchive(clientId, archive);
	}
	
	public List<Category> findByClientIdAndStatusAndArchive(Integer clientId, boolean status, boolean archive) throws SilException {
		return categoryRepository.findByClientIdAndStatusAndArchive(clientId, status, archive);
	}

	public Category findByCategoryIdAndArchive(Integer categoryId, boolean archive) throws SilException {
		return categoryRepository.findByCategoryIdAndArchive(categoryId, archive);
	}

	public boolean save(Category category) {
		Category c = categoryRepository.save(category);
		if (c == null) {
			return false;
		}
		return true;
	}
}
