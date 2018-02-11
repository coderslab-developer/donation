package com.sil.donation.model;

/**
 * @author Zubayer Ahamed
 *
 */
public class AdminDashboard {

	private Integer totalSellOfSoftware;
	private Integer totalDealerOfSoftware;
	private Integer activeClient;
	private Integer serviceRenewCurrentMotnh;
	private Integer deactiveClient;

	public Integer getTotalSellOfSoftware() {
		return totalSellOfSoftware;
	}

	public void setTotalSellOfSoftware(Integer totalSellOfSoftware) {
		this.totalSellOfSoftware = totalSellOfSoftware;
	}

	public Integer getTotalDealerOfSoftware() {
		return totalDealerOfSoftware;
	}

	public void setTotalDealerOfSoftware(Integer totalDealerOfSoftware) {
		this.totalDealerOfSoftware = totalDealerOfSoftware;
	}

	public Integer getActiveClient() {
		return activeClient;
	}

	public void setActiveClient(Integer activeClient) {
		this.activeClient = activeClient;
	}

	public Integer getServiceRenewCurrentMotnh() {
		return serviceRenewCurrentMotnh;
	}

	public void setServiceRenewCurrentMotnh(Integer serviceRenewCurrentMotnh) {
		this.serviceRenewCurrentMotnh = serviceRenewCurrentMotnh;
	}

	public Integer getDeactiveClient() {
		return deactiveClient;
	}

	public void setDeactiveClient(Integer deactiveClient) {
		this.deactiveClient = deactiveClient;
	}

	@Override
	public String toString() {
		return "AdminDashboard [totalSellOfSoftware=" + totalSellOfSoftware + ", totalDealerOfSoftware="
				+ totalDealerOfSoftware + ", activeClient=" + activeClient + ", serviceRenewCurrentMotnh="
				+ serviceRenewCurrentMotnh + ", deactiveClient=" + deactiveClient + "]";
	}

}
