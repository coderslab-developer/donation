/**
 * 
 */
package com.sil.donation.entity;

import java.io.Serializable;
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
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "smstransaction", catalog = "dms")
@Data
public class SMSTransaction implements Serializable {

	private static final long serialVersionUID = 283044380885966115L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "username", length = 40)
	private String username;

	@Column(name = "availableSMS")
	private int availableSMS;

	@Column(name = "usedSMS")
	private int usedSMS;

	@Column(name = "actionPerform")
	private String actionPerform;

	@Temporal(TemporalType.DATE)
	@Column(name = "actionDate", nullable = false, length = 10)
	private Date actionDate;

	@Column(name = "smsKey", length = 65535)
	private String smsKey; 

}
