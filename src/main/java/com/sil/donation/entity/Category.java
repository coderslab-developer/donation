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

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "category", catalog = "dms")
@Data
public class Category implements Serializable {

	private static final long serialVersionUID = 8762132733306871437L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryId", unique = true, nullable = false)
	private Integer categoryId;

	@NotEmpty(message = "Please enter category name")
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Min(value = 1, message = "You hvae to select atleast one day to create category")
	@Column(name = "days", nullable = false)
	private int days;

	@Column(name = "clientId", nullable = false)
	private Integer clientId;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

}
