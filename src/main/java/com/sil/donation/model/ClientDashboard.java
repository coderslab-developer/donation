package com.sil.donation.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
public class ClientDashboard {

	private Integer totalDonar;
	private Integer activeDonar;
	private Integer inactiveDonar;
	private Integer numberOfPayeeDonarInThisMonth;

}
