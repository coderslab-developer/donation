/**
 * 
 */
package com.sil.donation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "siteconfig", catalog = "dms")
@Data
public class SiteConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "adminId")
	private Integer adminId;

	@Column(name = "dealerId")
	private Integer dealerId;

	@Column(name = "clientId")
	private Integer clientId;

	@Column(name = "siteCustomLogo")
	private String siteCustomLogo;

	@Column(name = "siteDefaultLogo")
	private String siteDefaultLogo;
}
