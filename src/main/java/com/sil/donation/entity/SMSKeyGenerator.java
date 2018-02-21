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

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "smskey", catalog = "dms")
@Data
public class SMSKeyGenerator implements Serializable {

	private static final long serialVersionUID = -2868420150859610347L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "smsKey", length = 255)
	private String smsKey;

	@Column(name = "status", columnDefinition = "BOOLEAN")
	private boolean status;

}
