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
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "csui", catalog = "dms")
@Data
public class ClientServiceUpdateInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "clientId", nullable = false)
	private Integer clientId;

	@Column(name = "updateBy", nullable = false, length = 50)
	private String updateBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updateDate", nullable = false, length = 10)
	private Date updateDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "nextExpireDate", nullable = false, length = 10)
	private Date nextExpireDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "previousExpireDate", nullable = false, length = 10)
	private Date previousExpireDate;

	@Transient
	private String clientName;
}
