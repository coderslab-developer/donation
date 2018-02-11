package com.sil.donation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "authorities", catalog = "dms")
public class Authorities implements Serializable {

	private static final long serialVersionUID = -5868673689552689079L;
	private Integer id;
	@NotEmpty(message = "Please enter username")
	private String username;
	@NotEmpty(message = "Please define authority")
	private String authority;
	private boolean archive;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "username", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "authority", nullable = false, length = 45)
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
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
		return "Authorities [id=" + id + ", username=" + username + ", authority=" + authority + ", archive=" + archive
				+ "]";
	}

}
