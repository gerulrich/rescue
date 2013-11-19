package net.cloudengine.util;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HexStringTest {

	static {
		TestCoverageUtil.reflectiveInvokePrivateConstructor(HexString.class);
	}
	
	private String plainText;
	private String encodedText;
	
	@Parameters
	public static Collection<Object[]> data() {

		Object[][] data = {
		   // texto plano             | texto en base 16.
		   { "Hello, World!",          "48656c6c6f2c20576f726c6421" },
		   { "This is a Test.",        "54686973206973206120546573742e" },
		   { "This is another test.",  "5468697320697320616e6f7468657220746573742e" },
		   { "admin",                  "61646d696e" },
		   { "I need a break!",        "49206e656564206120627265616b21" }
		};
		return Arrays.asList(data);
	}
	
	public HexStringTest(String plainText, String encodedText) {
		super();
		this.plainText = plainText;
		this.encodedText = encodedText;
	}
	
	@Test
	public void test_encode() {
		String encodedResult = HexString.encode(plainText);
		assertEquals(this.encodedText, encodedResult);
	}
	
	@Test
	public void test_decode() {
		String encodedResult = HexString.decode(this.encodedText);
		assertEquals(plainText, encodedResult);
	}
}
