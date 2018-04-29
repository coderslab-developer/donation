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

	/**
	 * Find All {@link Category}
	 * @return List<{@link Category}>
	 */
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	/**
	 * Find All {@link Category} by ClientId and Archive
	 * @param clientId
	 * @param archive
	 * @return List<{@link Category}>
	 * @throws SilException
	 */
	public List<Category> findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException {
		return categoryRepository.findByClientIdAndArchive(clientId, archive);
	}

	/**
	 * Find All {@link Category} by ClientId, Status and Archive
	 * @param clientId
	 * @param status
	 * @param archive
	 * @return List<{@link Category}>
	 * @throws SilException
	 */
	public List<Category> findByClientIdAndStatusAndArchive(Integer clientId, boolean status, boolean archive) throws SilException {
		return categoryRepository.findByClientIdAndStatusAndArchive(clientId, status, archive);
	}

	/**
	 * Find {@link Cateogyr} by CategoryId and Archive
	 * @param categoryId
	 * @param archive
	 * @return {@link Cateogyr}
	 * @throws SilException
	 */
	public Category findByCategoryIdAndArchive(Integer categoryId, boolean archive) throws SilException {
		return categoryRepository.findByCategoryIdAndArchive(categoryId, archive);
	}

	/**
	 * Save {@link Cateogry}
	 * @param category
	 * @return {@literal boolean}
	 */
	public boolean save(Category category) {
		Category c = categoryRepository.save(category);
		return c == null ? false : true;
	}
}
