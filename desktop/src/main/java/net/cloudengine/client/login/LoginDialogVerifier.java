package net.cloudengine.client.login;

public interface LoginDialogVerifier {
	
	void authenticate(final String login, final String password) throws Exception;

}
