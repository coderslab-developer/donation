/**
 * 
 */
package com.sil.donation.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
public class SMSNotifier {

	private String fullName;
	private int availableSMS;
	private int activeSMSClient;
	private String message;

}
