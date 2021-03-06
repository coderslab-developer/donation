package com.sil.donation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "users", catalog = "dms")
@Data
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = 3648702449390303220L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId", unique = true, nullable = false)
	private Integer userId;

	@NotEmpty(message = "Please enter username")
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String username;

	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "password", nullable = false, length = 65535)
	private String password;

	@NotEmpty(message = "Please define role")
	@Column(name = "authority", nullable = false, length = 45)
	private String authority;

	@Column(name = "enabled", columnDefinition = "BOOLEAN")
	private boolean enabled;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

	@Transient
	private String name;

	@Transient
	private String mobile;

	@Transient
	private Date registerDate;

	@Transient 
	private String role;

	@Transient
	private String address;

	@Transient
	private Integer dealerId;

	@Transient
	private Integer adminId;

	@Transient
	private boolean smsService;
}
