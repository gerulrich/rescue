package net.cloudengine.client.login;

import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.SigninService;

import com.caucho.hessian.client.MyHessianProxyFactory;
import com.google.inject.Inject;


public class LoginDialogVerifierImpl implements LoginDialogVerifier {
	
	private SigninService service;
	private Context context;
	
	@Inject
	public LoginDialogVerifierImpl(SigninService service, Context context) {
		super();
		this.service = service;
		this.context = context;
	}



	@Override
	public void authenticate(final String login, final String password) throws Exception {
		
		if ("".equals(login)) {
			throw new Exception("Please enter a login.");
		}
	
		if ("".equals(password)) {
			throw new Exception("Please enter a password.");
		}

		String sessionId = service.login(login, password);
		if (sessionId == null) {
			throw new Exception("Compruebe su usuario y su contraseña.");
		} else {
			MyHessianProxyFactory.setSessionId(sessionId);
			context.setCurrentUser(service.getUserDetail());
		}
	}
	
}

