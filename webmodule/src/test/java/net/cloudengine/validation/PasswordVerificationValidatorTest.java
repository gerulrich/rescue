package net.cloudengine.validation;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class PasswordVerificationValidatorTest {


	private String password;
	private String passwordVerification;
	private boolean validPassword;
	
	public PasswordVerificationValidatorTest(String password, String passwordVerification, boolean validPassword) {
		super();
		this.password = password;
		this.passwordVerification = passwordVerification;
		this.validPassword = validPassword;
	}

	@Parameters
	public static Collection<Object[]> data() {
		
		Object[][] data = {
			{ "pass1", "pass1", true},
			{ "pass1", "pass2", false},
			{ null, null, false},
			{ null, "pass", false},
			{ "pass", null, false}
		};
		
		return Arrays.asList(data);
		
	}
	
	@Test
	public void passwordVerificationTest() {
		PasswordVerificationValidator validator = new PasswordVerificationValidator();
		validator.initialize(null);
		
		PasswordVerificable verificable = Mockito.mock(PasswordVerificable.class);
		Mockito.when(verificable.getPassword()).thenReturn(password);
		Mockito.when(verificable.getPasswordVerification()).thenReturn(passwordVerification);
		
		boolean validResult = validator.isValid(verificable, null);
		assertEquals(this.validPassword, validResult);
	}

}
