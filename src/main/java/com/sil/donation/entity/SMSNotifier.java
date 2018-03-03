/**
 * 
 */
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
@Table(name = "smsnotifier")
@Data
public class SMSNotifier implements Serializable {

	private static final long serialVersionUID = -7057861664914165185L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@NotEmpty(message = "Please enter username")
	@Column(name = "username", nullable = false, length = 40)
	private String username;

	@NotEmpty(message = "Please enter notification message")
	@Column(name = "message", nullable = false, length = 255)
	private String message;

	@Column(name = "availableSMS")
	private int availableSMS;

	@Column(name = "availableSMSClient")
	private int availableSMSClient;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;
}
