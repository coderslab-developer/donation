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

/**
 * @author Zubayer Ahamed
 *
 */

@Entity
@Table(name = "client", catalog = "dms")
public class Client implements Serializable {

	private static final long serialVersionUID = 2688296504512493855L;
	private Integer clientId;
	@NotEmpty(message = "Please enter client/institute name")
	private String clientName;
	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	private String email;
	@Size(min = 11, max = 11, message = "Mobile Number must be 11 character")
	private String mobile;
	private String fax;
	private String website;
	private Date registerDate;
	private Date expireDate;
	@NotEmpty(message = "Please enter client address")
	private String address;
	private boolean status;
	private String photo;
	private Integer dealerId;
	private String username;
	private String password;
	private String logo;
	private String dealerName;
	private List<Donar> donars;
	private boolean archive;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clientId", unique = true, nullable = false)
	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	@Column(name = "clientName", nullable = false, length = 100)
	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "email", nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile", nullable = false, length = 11)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "fax", length = 45)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "website", length = 100)
	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "expireDate", nullable = false, length = 10)
	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "address", nullable = false, length = 65535)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "status")
	public boolean isStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Column(name = "photo", length = 65535)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "dealerId", nullable = false)
	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	@Column(name = "username", nullable = false, length = 100)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public List<Donar> getDonars() {
		return donars;
	}

	public void setDonars(List<Donar> donars) {
		this.donars = donars;
	}

	@Column(name = "logo", length = 225)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Transient
	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	@Column(name = "archive")
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", clientName=" + clientName + ", email=" + email + ", mobile=" + mobile
				+ ", fax=" + fax + ", website=" + website + ", registerDate=" + registerDate + ", expireDate="
				+ expireDate + ", address=" + address + ", status=" + status + ", photo=" + photo + ", dealerId="
				+ dealerId + ", username=" + username + ", password=" + password + ", logo=" + logo + ", dealerName="
				+ dealerName + ", donars=" + donars + ", archive=" + archive + "]";
	}

}
