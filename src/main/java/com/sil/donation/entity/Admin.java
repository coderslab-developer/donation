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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "admin", catalog = "dms")
@Data
@XmlRootElement(name = "admin")
@XmlAccessorType(XmlAccessType.FIELD)
public class Admin implements Serializable {

	private static final long serialVersionUID = -8327881487089084713L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "adminId", unique = true, nullable = false)
	private Integer adminId;

	@Column(name = "adminName", nullable = false, length = 100)
	private String adminName;

	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "username", unique = true, nullable = false, length = 40)
	private String username;

	@Column(name = "password", nullable = false, length = 65535)
	private String password;

	@Size(min = 11, max = 11, message = "Mobile number must be 11 character")
	@Column(name = "mobile", nullable = false, length = 11)
	private String mobile;

	@Column(name = "address", nullable = false, length = 65535)
	private String address;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", length = 10)
	private Date registerDate;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

	@Transient
	@XmlElementWrapper(name = "dealers")
	@XmlElement(name = "dealer")
	private List<Dealer> dealers;

	@Transient
	private Integer totalSellOfSoftware;

	@Transient
	private Integer totalDealerOfSoftware;

	@Transient
	private Integer activeClient;

	@Transient
	private Integer serviceRenewCurrentMotnh;

	@Transient
	private Integer inactiveClients;
}
