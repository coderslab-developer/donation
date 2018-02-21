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

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "admin", catalog = "dms")
@Data
public class Admin implements Serializable {

	private static final long serialVersionUID = -8327881487089084713L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adminId", unique = true, nullable = false)
	private Integer adminId;

	@Column(name = "adminName", nullable = false, length = 100)
	private String adminName;

	@Column(name = "email", unique = true, nullable = false, length = 100)
	private String email;

	@Column(name = "username", unique = true, nullable = false, length = 40)
	private String username;

	@Column(name = "password", nullable = false, length = 20)
	private String password;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	private Date registerDate;

	@Transient
	private List<Dealer> dealers;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

}
