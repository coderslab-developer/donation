package com.sil.donation.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
public class AdminDashboard {

	private Integer totalSellOfSoftware;
	private Integer totalDealerOfSoftware;
	private Integer activeClient;
	private Integer serviceRenewCurrentMotnh;
	private Integer inactiveClients;

}
