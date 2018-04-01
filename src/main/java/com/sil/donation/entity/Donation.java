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
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sil.donation.util.DateAdapter;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Entity
@Table(name = "donation", catalog = "dms")
@Data
@XmlRootElement(name = "donation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Donation implements Serializable {

	private static final long serialVersionUID = -7098338410742262751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donationId", unique = true, nullable = false)
	private Integer donationId;

	@Column(name = "donarId", nullable = false)
	private Integer donarId;

	@Min(value = 1, message = "Please enter positive amount")
	@Column(name = "payableAmount", nullable = false)
	private double payableAmount;

	@Column(name = "paid", nullable = false)
	private double paid;

	@Column(name = "due", nullable = false)
	private double due;

	@Temporal(TemporalType.DATE)
	@Column(name = "payDate", nullable = false, length = 10)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date payDate;

	@Column(name = "clientId", nullable = false)
	private Integer clientId;

	@Transient
	private List<Donar> donars;

	@Transient
	private String donarName;

}
