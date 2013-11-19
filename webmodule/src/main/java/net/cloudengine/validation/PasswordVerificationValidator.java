package net.cloudengine.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordVerificationValidator implements ConstraintValidator<PasswordVerification, PasswordVerificable> {

	public void initialize(PasswordVerification constraintAnnotation) {
		// no hago nada
	}

	public boolean isValid(PasswordVerificable value, ConstraintValidatorContext context) {
		if ( value.getPassword() == null && value.getPasswordVerification() == null ) {
			return false;
		}
		else if ( value.getPassword() == null ) {
			return false;
		}
		return ( value.getPassword().equals(value.getPasswordVerification()));
	}
}
