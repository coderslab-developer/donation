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
@Entity
@Table(name = "smsaccount", catalog = "dms")
@Data
public class SMSAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "smsAccountId", nullable = false, unique = true)
	private Integer smsAccountId;

	@Column(name = "secretName", length = 20, nullable = false, unique = true)
	private String secretName;

	@Column(name = "accessId", length = 65535)
	private String accessId;

	@Column(name = "accessKey", length = 65535)
	private String accessKey;

	@Column(name = "apikey", length = 65535)
	private String apiKey;

	@Column(name = "baseUrl", length = 65535)
	private String baseUrl;

	@Temporal(TemporalType.DATE)
	@Column(name = "createDate", nullable = false, length = 10)
	private Date createDate;

	@Column(name = "active", columnDefinition = "BOOLEAN")
	private Boolean active;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private Boolean archive;
}
