/**
 * 
 */
package com.sil.donation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public class RandomNumberGenerator {

	public String generateKey() {
		String firstDigit = String.valueOf(Math.abs(new Random().nextLong()));
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String key = firstDigit + sdf.format(new Date()) + Math.abs(new Random().nextInt(100));

		return key;
	}
}
