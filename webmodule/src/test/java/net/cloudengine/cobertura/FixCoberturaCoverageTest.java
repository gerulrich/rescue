package net.cloudengine.cobertura;

import javax.validation.ConstraintValidatorContext;

import net.cloudengine.util.TestCoverageUtil;
import net.cloudengine.validation.EmailValidator;
import net.cloudengine.validation.PasswordVerificable;
import net.cloudengine.validation.PasswordVerificationValidator;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Este test (que en realidad no es un test), se encarga
 * de corregir la cobertura de las clases que tienen Generics.
 * Estas clases contienen metodos de tipo "bridge" ocultos. Los test
 * se encargan de ejecutar estos m&eacute;todos para corregir la cobertura de la clase. 
 * @author German Ulrich
 *
 */
public class FixCoberturaCoverageTest {
	
	@Test
	public void fixEmailValidatorCoverage() {
		EmailValidator validator = new EmailValidator();
		Class<?> argTypes[] = {Object.class, ConstraintValidatorContext.class};
		TestCoverageUtil.reflectiveInvokeHiddenBridgeMethod(validator, "isValid", argTypes, "", null);
	}
	
	@Test
	public void fixPasswordVerificableValidatorCoverage() {
		PasswordVerificationValidator validator = new PasswordVerificationValidator();
		Class<?> argTypes[] = {Object.class, ConstraintValidatorContext.class};
		PasswordVerificable verificable = Mockito.mock(PasswordVerificable.class);
		TestCoverageUtil.reflectiveInvokeHiddenBridgeMethod(validator, "isValid", argTypes, verificable, null);
	}	

}
