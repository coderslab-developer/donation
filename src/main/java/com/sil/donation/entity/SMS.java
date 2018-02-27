/**
 * 
 */
package com.sil.donation.entity;

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

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "sms")
@Data
public class SMS {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "smsId", nullable = false, unique = true)
	private Long smsId;

	@Column(name = "smsContent", nullable = false, length = 65535)
	private String smsContent;

	@Column(name = "mobileNumber", length = 65535)
	private String mobileNumber;

	@Column(name = "smsCharacter", nullable = false)
	private int smsCharacter;

	@Column(name = "smsLength", nullable = false)
	private int smsLength;

	@Column(name = "smsType", length = 20, nullable = false)
	private String smsType;

	@Column(name = "maskName", length = 40)
	private String maskName;

	@Temporal(TemporalType.DATE)
	@Column(name = "sendDate", length = 10)
	private Date sendDate;

	@Column(name = "userName", nullable = false, length = 40)
	private String userName;

	@Column(name = "userPassword", nullable = false, length = 40)
	private String userPassword;

	@Column(name = "campaignName", length = 50)
	private String campaignName;

	@Transient
	private List<String> numberList;
}
