/**
 * 
 */
package com.sil.donation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * @author zubayer
 *
 */
@Data
@Entity
@Table(name = "smsinfotracker", catalog = "dms")
public class SMSInfoTracker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", length = 100)
	private String code;

	@Column(name = "mobileNumber", length = 100)
	private String mobileNumber;

	@Column(name = "referenceCode", length = 100)
	private String referenceCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "smsDateTime", nullable = false, length = 20)
	private Date smsDateTime;
}
