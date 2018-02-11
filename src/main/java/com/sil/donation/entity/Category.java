package com.sil.donation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "category", catalog = "dms")
public class Category implements Serializable {

	private static final long serialVersionUID = 8762132733306871437L;

	private Integer categoryId;
	@NotEmpty(message = "Please enter category name")
	private String name;
	@Min(value = 1, message = "You hvae to select atleast one day to create category")
	private int days;
	private Integer clientId;
	private boolean status;
	private boolean archive;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryId", unique = true, nullable = false)
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "days", nullable = false)
	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	@Column(name = "clientId", nullable = false)
	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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
		return "Category [categoryId=" + categoryId + ", name=" + name + ", days=" + days + ", clientId=" + clientId
				+ ", status=" + status + ", archive=" + archive + "]";
	}

}
