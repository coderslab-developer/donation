/**
 * 
 */
package com.sil.donation.entity;

import java.io.Serializable;

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
public class SiteConfig implements Serializable {

	private static final long serialVersionUID = -2758895369807530338L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username")
	private String username;

	@Column(name = "siteName", length = 40)
	private String siteName;

	@Column(name = "logo", length = 65535)
	private String logo;

	@Column(name = "enableLogo", columnDefinition = "BOOLEAN")
	private boolean enableLogo;
}
