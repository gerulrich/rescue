package net.cloudengine.util;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test para probar que el Cifrador SHA funciona correctemente. Se utilizan varios
 * textos predefinidos con sus valores correctos de hash.
 * 
 * @author German Ulrich
 * 
 */
@RunWith(Parameterized.class)
public class CipherTest extends TestCase {

	private String plainText;
	private String cipherText;

	@Parameters
	public static Collection<Object[]> data() {

		Object[][] data = {
		   // texto plano             | texto cifrado.
		   { "Hello, World!",          "0a0a9f2a6772942557ab5355d76af442f8f65e01" },
		   { "This is a Test.",        "6b07fc8662078927d0eaf1f1514e531e0b01efe6" },
		   { "This is another test.",  "8d5dfa91327468b86c69e3c7880c1d6ceaa8f1eb" },
		   { "admin",                  "d033e22ae348aeb5660fc2140aec35850c4da997" },
		   { "I need a break!",        "2404a444c6f5e319233686846d20c5e5aa36adc0" }
		};
		return Arrays.asList(data);
	}

	public CipherTest(String plainText, String cipherText) {
		super();
		this.plainText = plainText;
		this.cipherText = cipherText;
	}

	@Test
	public void testEncrypt() {
		String pp = new Cipher().encrypt(this.plainText);
		assertEquals("Error al calcular el Hash de :"+ this.plainText, this.cipherText, pp);
	}

}
