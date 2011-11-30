package net.cloudengine.util;

import org.junit.Test;

public class FailedCipherTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void fail() {
		String clearText = "Failed test";
		String alg = "SHA2";
		new Cipher().encrypt(clearText, alg);
	}

}
