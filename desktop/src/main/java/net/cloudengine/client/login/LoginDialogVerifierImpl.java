package net.cloudengine.client.login;

import java.io.IOException;

import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.SigninService;

import com.caucho.hessian.client.HessianConnectionException;
import com.caucho.hessian.client.HessianRuntimeException;
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

		try {
			String sessionId = service.login(login, password);
			if (sessionId == null) {
				throw new AuthenticationException("Compruebe su usuario y su contraseña.");
			} else {
				MyHessianProxyFactory.setSessionId(sessionId);
				context.setCurrentUser(service.getUserDetail());
			}
//		} catch (IOException e) {
//			throw new AuthenticationException("No se pudo establecer la conexión con el servidor");
		}  catch (HessianRuntimeException e) {
				Throwable t = e.getCause();
				if (t instanceof IOException) {
					throw new RuntimeException("No se pudo establecer la conexión con el servidor");
				}
		} catch (Exception e) {
			try {
				throw e;
//			} catch (IOException ex) {
//				throw new AuthenticationException("Se produjo un erorr en la conexión con el servidor");
//			} catch (HessianProtocolException ex) {
			} catch (HessianConnectionException ex) {
				throw new AuthenticationException("No se pudo establecer la conexión con el servidor");
			} catch (AuthenticationException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new AuthenticationException(ex.getMessage());
			}
		}
	}
}

