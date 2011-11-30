package net.cloudengine.forms

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import net.cloudengine.validation.Email
import net.cloudengine.validation.PasswordVerificable
import net.cloudengine.validation.PasswordVerification


/**
 * Entidad que representa los datos del formulario para un nuevo usuaro.
 * @author German Ulrich
 *
 */
@PasswordVerification
class NewUser implements Serializable, PasswordVerificable {

	@NotNull
	@Pattern(regexp=".+")
	String displayName;

	@Email
	String email;

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
