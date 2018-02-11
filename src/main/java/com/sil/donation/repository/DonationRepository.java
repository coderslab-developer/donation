package com.sil.donation.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sil.donation.entity.Donation;

/**
 * @author Zubayer Ahamed
 *
 */
@Repository
@Transactional
public interface DonationRepository extends JpaRepository<Donation, Integer>{

}
