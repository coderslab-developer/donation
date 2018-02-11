package com.sil.donation.entity;

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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "dealer", catalog = "dms", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username") })
public class Dealer implements java.io.Serializable {

	private static final long serialVersionUID = 7619514728719338221L;
	private Integer dealerId;
	@NotEmpty(message = "Please enter dealer name")
	private String dealerName;
	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	private String email;
	@NotEmpty(message = "Please enter username")
	private String username;
	private String password;
	@NotEmpty(message = "Please enter address")
	private String address;
	@Size(min = 11, max = 11, message = "Mobile number must be 11 character")
	private String mobile;
	private Date registerDate;
	private Date updateDate;
	private boolean status;
	private String logo;
	private String photo;
	private List<Client> clients;
	private Integer activeClients;
	private Integer deactiveClients;
	private boolean archive;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dealerId", unique = true, nullable = false)
	public Integer getDealerId() {
		return this.dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	@Column(name = "dealerName", nullable = false, length = 100)
	public String getDealerName() {
		return this.dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	@Column(name = "email", unique = true, nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "address", nullable = false, length = 65535)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "mobile", nullable = false, length = 11)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	@Column(name = "updateDate", length = 10)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Column(name = "username", unique = true, nullable = false, length = 40)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	@Transient
	public Integer getActiveClients() {
		return activeClients;
	}

	public void setActiveClients(Integer activeClients) {
		this.activeClients = activeClients;
	}

	@Transient
	public Integer getDeactiveClients() {
		return deactiveClients;
	}

	public void setDeactiveClients(Integer deactiveClients) {
		this.deactiveClients = deactiveClients;
	}

	@Column(name = "logo", length = 225)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "archive")
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@Column(name = "photo", length = 225)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Dealer [dealerId=" + dealerId + ", dealerName=" + dealerName + ", email=" + email + ", username="
				+ username + ", password=" + password + ", address=" + address + ", mobile=" + mobile
				+ ", registerDate=" + registerDate + ", updateDate=" + updateDate + ", status=" + status + ", logo="
				+ logo + ", photo=" + photo + ", clients=" + clients + ", activeClients=" + activeClients
				+ ", deactiveClients=" + deactiveClients + ", archive=" + archive + "]";
	}

}
