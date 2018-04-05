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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sil.donation.util.DateAdapter;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "dealer", catalog = "dms", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username") })
@Data
@XmlRootElement(name = "dealer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Dealer implements Serializable {

	private static final long serialVersionUID = 7619514728719338221L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dealerId", unique = true, nullable = false)
	private Integer dealerId;

	@Column(name = "adminId", nullable = false)
	private Integer adminId;

	@NotEmpty(message = "Please enter dealer name")
	@Column(name = "dealerName", nullable = false, length = 100)
	private String dealerName;

	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@NotEmpty(message = "Please enter username")
	@Column(name = "username", unique = true, nullable = false, length = 40)
	private String username;

	@Column(name = "password", nullable = false, length = 65535)
	private String password;

	@NotEmpty(message = "Please enter address")
	@Column(name = "address", nullable = false, length = 65535)
	private String address;

	@Size(min = 11, max = 11, message = "Mobile number must be 11 character")
	@Column(name = "mobile", nullable = false, length = 11)
	private String mobile;

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date registerDate;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Column(name = "photo", length = 225)
	private String photo;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

	@Transient
	@XmlElementWrapper(name = "clients")
	@XmlElement(name = "client")
	private List<Client> clients;

	@Transient
	private Integer activeClients;

	@Transient
	private Integer inactiveClients;

	@Transient
	private Integer totalSellOfSoftware;

	@Transient
	private Integer serviceRenewOnThisMonth;

	@Transient
	private String siteLogo;

	@Transient
	private String reportName;
}
