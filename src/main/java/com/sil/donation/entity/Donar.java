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
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotEmpty;

import com.sil.donation.util.DateAdapter;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "donar", catalog = "dms", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
@Data
@XmlRootElement(name = "donar")
@XmlAccessorType(XmlAccessType.FIELD)
public class Donar implements Serializable {

	private static final long serialVersionUID = 7898141064460416531L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donarId", unique = true, nullable = false)
	private Integer donarId;

	@NotEmpty(message = "Please enter donar name")
	@Column(name = "donarName", nullable = false, length = 100)
	private String donarName;

	@Column(name = "clientId", nullable = false)
	private Integer clientId;

	@Column(name = "categoryId")
	private Integer categoryId;

	@Column(name = "address", nullable = false, length = 65535)
	private String address;

	@Size(min = 11, max = 11, message = "Mobile number should be 11 character")
	@Column(name = "mobile", nullable = false, length = 11)
	private String mobile;

	@Min(value = 1, message = "Plese enter amount to create donar")
	@Column(name = "payableAmount")
	private double payableAmount;

	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "instituteName", nullable = false, length = 255)
	private String instituteName;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Column(name = "smsService", columnDefinition = "BOOLEAN")
	private boolean smsService;

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date registerDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "smsDate", length = 10)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date smsDate;

	@Column(name = "photo", length = 225)
	private String photo;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

	@Transient
	private List<Category> categories;

	@Transient
	private String categoryName;
	
	@Column(name = "autoMessage", length = 65535)
	private String autoMessage;

	@Column(name = "maskName", length = 50)
	private String maskName;

	@Column(name = "campaignName", length = 50)
	private String campaignName;
}
