package net.cloudengine.validation;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class EmailValidatorTest extends TestCase {

	private String email;
	private boolean validEmail;
	
	@Parameters
	public static Collection<Object[]> data() {

		Object[][] data = {
		   // email a verificar             | si es valido o no.
		   {"test@yahoo.com",              true },
		   {"test-100@yahoo.com",          true }, 
		   {"test.100@yahoo.com",          true },
		   {"test111@test.com",            true },
		   {"test-100@test.net",           true },
		   {"test.100@test.com.au",        true },
		   {"test@1.com",                  true },
		   {"test@gmail.com.com",          true },
		   {"test",                        false},
		   {"test@.com.ar",                false},
		   {"test123@gmail.a",             false}, 
		   {"test123@.com",                false}, 
		   {"test123@.com.com",            false},
		   {".test@test.com",              false},
		   {"test()*@gmail.com",           false},
		   {"test@%*.com",                 false},
		   {"test..2002@gmail.com",        false}, 
		   {"test.@gmail.com",             false},
		   {"test@test@gmail.com",         false}, 
		   {"test@gmail.com.1a",           false},
		   {null,                          false}
		};
		return Arrays.asList(data);
	}
	
	public EmailValidatorTest(String email, boolean validEmail) {
		super();
		this.email = email;
		this.validEmail = validEmail;
	}

	@Test
	public void emailVerificationTest() {
		EmailValidator validator = new EmailValidator();
		validator.initialize(null);		
		boolean validResult = validator.isValid(this.email, null);
		assertEquals(this.validEmail, validResult);
	}

}