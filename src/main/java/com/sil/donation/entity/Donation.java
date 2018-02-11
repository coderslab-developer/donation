package com.sil.donation.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "donation", catalog = "dms")
public class Donation implements Serializable {

	private static final long serialVersionUID = -7098338410742262751L;

	private Integer donationId;
	private Integer donarId;
	@Min(value = 1, message = "Please enter positive amount")
	private double payableAmount;
	private double paid;
	private double due;
	private Date payDate;
	private List<Donar> donars;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donationId", unique = true, nullable = false)
	public Integer getDonationId() {
		return donationId;
	}

	public void setDonationId(Integer donationId) {
		this.donationId = donationId;
	}

	@Column(name = "donarId", nullable = false)
	public Integer getDonarId() {
		return donarId;
	}

	public void setDonarId(Integer donarId) {
		this.donarId = donarId;
	}

	@Column(name = "payableAmount", nullable = false)
	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	@Column(name = "paid", nullable = false)
	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	@Column(name = "due", nullable = false)
	public double getDue() {
		return due;
	}

	public void setDue(double due) {
		this.due = due;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "payDate", nullable = false, length = 10)
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public List<Donar> getDonars() {
		return donars;
	}

	public void setDonars(List<Donar> donars) {
		this.donars = donars;
	}

	@Override
	public String toString() {
		return "Donation [donationId=" + donationId + ", donarId=" + donarId + ", payableAmount=" + payableAmount
				+ ", paid=" + paid + ", due=" + due + ", payDate=" + payDate + ", donars=" + donars + "]";
	}

}
