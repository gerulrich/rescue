package net.cloudengine.client.main;

import net.cloudengine.client.login.LoginDialog;
import net.cloudengine.client.login.LoginDialogVerifierImpl;
import net.cloudengine.client.resources.ImgBundle;
import net.cloudengine.rpc.controller.auth.SigninService;
import net.cloudengine.rpc.controller.config.PropertyController;

import org.eclipse.swt.widgets.Display;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	public static void main(String[] args) {
		
		Injector injector = Guice.createInjector(GuiceModule.BASE);
		SigninService signin = injector.getInstance(SigninService.class);
		
		/*Display display = */new Display();

		final LoginDialog dialog = new LoginDialog();
		dialog.setDisplayRememberPassword(false);
		dialog.setDescription("Ingrese su usuario y contraseña para acceder a la aplicación");
		
		dialog.setVerifier(injector.getInstance(LoginDialogVerifierImpl.class));
		
		dialog.setImage(ImgBundle.getImgBundle().getImageDescriptor("login.png").createImage());

		final boolean result = dialog.open();
		if (result) {
		    System.out.println("Login confirmed : " + dialog.getLogin());
		    
		    Application app = new Application();
		    app.setPropertyController(injector.getInstance(PropertyController.class));
			app.setBlockOnOpen(true);
		    // Open the main window
		    app.open();
		    // Dispose the display
		    Display.getCurrent().dispose();
		    signin.logout(null);
		    
		    
		} else {
			System.out.println("User canceled !");
			return;
			
		}
	}
}
