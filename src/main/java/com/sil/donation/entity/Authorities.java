package com.sil.donation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "authorities", catalog = "dms")
@Data
public class Authorities implements Serializable {

	private static final long serialVersionUID = -5868673689552689079L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@NotEmpty(message = "Please enter username")
	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@NotEmpty(message = "Please define authority")
	@Column(name = "authority", nullable = false, length = 45)
	private String authority;

	@Column(name = "archive", columnDefinition = "BOOLEAN")
	private boolean archive;

}
