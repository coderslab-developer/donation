/**
 * 
 */
package com.sil.donation.model;

/**
 * @author Zubayer Ahamed
 *
 */
public enum UserAuthorities {
	ROLE_ADMIN("ADMIN"), ROLE_DEALER("DEALER"), ROLE_CLIENT("CLIENT");

	private String code;

	private UserAuthorities(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}
}
