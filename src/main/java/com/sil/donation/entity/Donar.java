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
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "donar", catalog = "dms", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class Donar {

	private Integer donarId;
	@NotEmpty(message = "Please enter donar name")
	private String donarName;
	private Integer clientId;
	private Integer categoryId;
	private String address;
	@Size(min = 11, max = 11, message = "Mobile number should be 11 character")
	private String mobile;
	@Min(value = 1, message = "Plese enter amount to create donar")
	private double payableAmount;
	private String email;
	private String instituteName;
	private boolean status;
	private boolean smsService;
	private Date registerDate;
	private List<Category> categories;
	private String categoryName;
	private boolean archive;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donarId", unique = true, nullable = false)
	public Integer getDonarId() {
		return donarId;
	}

	public void setDonarId(Integer donarId) {
		this.donarId = donarId;
	}

	@Column(name = "donarName", nullable = false, length = 100)
	public String getDonarName() {
		return donarName;
	}

	public void setDonarName(String donarName) {
		this.donarName = donarName;
	}

	@Column(name = "clientId", nullable = false)
	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	@Column(name = "categoryId")
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "address", nullable = false, length = 65535)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "mobile", nullable = false, length = 11)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "payableAmount")
	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	@Column(name = "email", unique = true, nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "instituteName", nullable = false, length = 255)
	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	@Column(name = "status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Column(name = "smsService")
	public boolean isSmsService() {
		return smsService;
	}

	public void setSmsService(boolean smsService) {
		this.smsService = smsService;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "registerDate", nullable = false, length = 10)
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "archive")
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@Transient
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	@Transient
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "Donar [donarId=" + donarId + ", donarName=" + donarName + ", clientId=" + clientId + ", categoryId="
				+ categoryId + ", address=" + address + ", mobile=" + mobile + ", payableAmount=" + payableAmount
				+ ", email=" + email + ", instituteName=" + instituteName + ", status=" + status + ", smsService="
				+ smsService + ", registerDate=" + registerDate + ", archive=" + archive + ", categories=" + categories
				+ ", categoryName=" + categoryName + "]";
	}

}
