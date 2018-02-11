package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Category;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByClientIdAndArchive(Integer clientId, boolean archive) throws SilException;

	Category findByCategoryIdAndArchive(Integer categoryId, boolean archive) throws SilException;

}
