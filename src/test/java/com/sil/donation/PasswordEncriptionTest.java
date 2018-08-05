/**
 * 
 */
package com.sil.donation;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zubayer Ahamed
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordEncriptionTest {

	private static final String PASSWORD = "admin";

	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

	@Ignore
	@Test
	public void encodePassword() {
		String pass1 = bCryptPasswordEncoder.encode(PASSWORD);
		System.out.println("password : " + pass1);
	}
}
