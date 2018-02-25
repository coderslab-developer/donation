package com.sil.donation.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
public class DealerDashboard {

	private Integer totalSellOfSoftware;
	private Integer activeClient;
	private Integer inactiveClient;
	private Integer serviceRenewOnThisMonth;

}
