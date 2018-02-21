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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */

@Entity
@Table(name = "client", catalog = "dms")
@Data
public class Client implements Serializable {

	private static final long serialVersionUID = 2688296504512493855L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clientId", unique = true, nullable = false)
	private Integer clientId;

	@NotEmpty(message = "Please enter client/institute name")
	@Column(name = "clientName", nullable = false, length = 100)
	private String clientName;

	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Size(min = 11, max = 11, message = "Mobile Number must be 11 character")
	@Column(name = "mobile", nullable = false, length = 11)
	private String mobile;

	@Column(name = "fax", length = 45)
	private String fax;

	@Column(name = "website", length = 100)
	private String website;

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	private Date registerDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "expireDate", nullable = false, length = 10)
	private Date expireDate;

	@NotEmpty(message = "Please enter client address")
	@Column(name = "address", nullable = false, length = 65535)
	private String address;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Column(name = "photo", length = 65535)
	private String photo;

	@Column(name = "dealerId", nullable = false)
	private Integer dealerId;

	@Column(name = "username", nullable = false, length = 100)
	private String username;

	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Column(name = "logo", length = 225)
	private String logo;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

	@Transient
	private String dealerName;

	@Transient
	private List<Donar> donars;

}
