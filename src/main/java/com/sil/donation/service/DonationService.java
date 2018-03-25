package com.sil.donation.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sil.donation.entity.Donation;
import com.sil.donation.repository.DonationRepository;

/**
 * @author Zubayer Ahamed
 *
 */
@Service
public class DonationService {

	@Autowired DonationRepository donationRepository;
	@Autowired EntityManager em;
	private static final String DONATION_SEARCH_QUERY = "SELECT d FROM Donation d WHERE d.clientId=:clientId AND d.payDate BETWEEN :startDate AND :endDate";

	public boolean save(Donation donation) {
		Donation d = donationRepository.save(donation);
		return d == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	public List<Donation> findAllDonationByClientIdAndPayDateBetweenStartDateToEndDate(Integer clientId, Integer donarId, Date startDate, Date endDate){
		String sql = DONATION_SEARCH_QUERY;

		if(donarId != null) {
			sql = sql + " AND d.donarId=:donarId";
		}

		Query query = em.createQuery(sql, Donation.class);
		query.setParameter("clientId", clientId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(donarId != null) {
			query.setParameter("donarId", donarId);
		}
		return query.getResultList();
	}
}
