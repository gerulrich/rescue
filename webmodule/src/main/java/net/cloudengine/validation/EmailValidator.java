package net.cloudengine.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

	
	
	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if (object == null) {
            return false;
		}
		return Pattern.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", object);
   }

	public void initialize(Email constraintAnnotation) {
		// no hago nada.
		return;
	}

}