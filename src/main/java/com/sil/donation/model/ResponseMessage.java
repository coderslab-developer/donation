package com.sil.donation.model;

/**
 * @author Zubayer Ahamed
 *
 */
public class ResponseMessage {

	public boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseMessage [status=" + status + "]";
	}

}
