package net.cloudengine.forms

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import net.cloudengine.validation.PasswordVerificable
import net.cloudengine.validation.PasswordVerification

@PasswordVerification
class PasswordForm implements Serializable, PasswordVerificable {
	
	@Size(min=5)
	@NotNull
	String password;
	
	@Size(min=5)
	@NotNull
	
	String passwordVerification;
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getPasswordVerification() {
		return passwordVerification;
	}

}
