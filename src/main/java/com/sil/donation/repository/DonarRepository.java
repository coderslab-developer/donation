package com.sil.donation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Donar;
import com.sil.donation.exception.SilException;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface DonarRepository extends JpaRepository<Donar, Integer> {

	List<Donar> findAllByClientIdAndArchive(Integer clientId, boolean archive) throws SilException;

	Donar findByDonarIdAndArchive(Integer donarId, boolean archive) throws SilException;
}
