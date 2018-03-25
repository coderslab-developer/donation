package com.sil.donation.model;

import java.util.Date;

import lombok.Data;

@Data
public class DonarTransactionReport {

	private String donarName;
	private Date payDate;
	private Double paid;
}
