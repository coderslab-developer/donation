package com.sil.donation.model;

/**
 * @author Zubayer Ahamed
 *
 */
public class ClientDashboard {

	private Integer totalDonar;
	private Integer activeDonar;
	private Integer inactiveDonar;
	private Integer numberOfPayeeDonarInThisMonth;

	public Integer getTotalDonar() {
		return totalDonar;
	}

	public void setTotalDonar(Integer totalDonar) {
		this.totalDonar = totalDonar;
	}

	public Integer getActiveDonar() {
		return activeDonar;
	}

	public void setActiveDonar(Integer activeDonar) {
		this.activeDonar = activeDonar;
	}

	public Integer getInactiveDonar() {
		return inactiveDonar;
	}

	public void setInactiveDonar(Integer inactiveDonar) {
		this.inactiveDonar = inactiveDonar;
	}

	public Integer getNumberOfPayeeDonarInThisMonth() {
		return numberOfPayeeDonarInThisMonth;
	}

	public void setNumberOfPayeeDonarInThisMonth(Integer numberOfPayeeDonarInThisMonth) {
		this.numberOfPayeeDonarInThisMonth = numberOfPayeeDonarInThisMonth;
	}

}
