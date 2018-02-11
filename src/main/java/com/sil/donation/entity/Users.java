package com.sil.donation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.UniqueConstraint;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "users", catalog = "dms", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username") })
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = 3648702449390303220L;
	private Integer userId;
	@NotEmpty(message = "Please enter username")
	private String username;
	@NotEmpty(message = "Please enter email address")
	@Email(message = "Please enter valid email address")
	private String email;
	@Size(min = 6, max = 20, message = "Password should be between 6 to 20 character")
	private String password;
	@NotEmpty(message = "Please define role")
	private String authority;
	private String photo;
	private boolean enabled;
	private boolean archive;

	public Users() {
	}

	public Users(String username, String email, String password, String authority) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.authority = authority;
	}

	public Users(String username, String email, String password, String authority, String photo, boolean enabled) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.authority = authority;
		this.photo = photo;
		this.enabled = enabled;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "username", unique = true, nullable = false, length = 45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", unique = true, nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", nullable = false, length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "authority", nullable = false, length = 45)
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Column(name = "photo", length = 65535)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "enabled")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		return "Users [userId=" + userId + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", authority=" + authority + ", photo=" + photo + ", enabled=" + enabled + ", archive=" + archive
				+ "]";
	}

}
