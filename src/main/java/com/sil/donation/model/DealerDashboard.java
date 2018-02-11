package com.sil.donation.model;

/**
 * @author Zubayer Ahamed
 *
 */
public class DealerDashboard {

	private Integer totalSellOfSoftware;
	private Integer activeClient;
	private Integer inactiveClient;
	private Integer serviceRenewOnThisMonth;

	public Integer getTotalSellOfSoftware() {
		return totalSellOfSoftware;
	}

	public void setTotalSellOfSoftware(Integer totalSellOfSoftware) {
		this.totalSellOfSoftware = totalSellOfSoftware;
	}

	public Integer getActiveClient() {
		return activeClient;
	}

	public void setActiveClient(Integer activeClient) {
		this.activeClient = activeClient;
	}

	public Integer getInactiveClient() {
		return inactiveClient;
	}

	public void setInactiveClient(Integer inactiveClient) {
		this.inactiveClient = inactiveClient;
	}

	public Integer getServiceRenewOnThisMonth() {
		return serviceRenewOnThisMonth;
	}

	public void setServiceRenewOnThisMonth(Integer serviceRenewOnThisMonth) {
		this.serviceRenewOnThisMonth = serviceRenewOnThisMonth;
	}

	@Override
	public String toString() {
		return "ClientDashboard [totalSellOfSoftware=" + totalSellOfSoftware + ", activeClient=" + activeClient
				+ ", inactiveClient=" + inactiveClient + ", serviceRenewOnThisMonth=" + serviceRenewOnThisMonth + "]";
	}

}
