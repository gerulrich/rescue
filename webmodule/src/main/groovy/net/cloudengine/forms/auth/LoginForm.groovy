package net.cloudengine.forms.auth

import javax.validation.constraints.NotNull;

import org.apache.bval.constraints.NotEmpty;

class LoginForm {
	
	@NotEmpty
	@NotNull
	String username;
	@NotNull
	@NotEmpty
	String password;
	boolean rememberme;
}
