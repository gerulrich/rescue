package net.cloudengine.web.home;

import javax.servlet.http.HttpServletRequest;

import net.cloudengine.forms.auth.LoginForm;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * El SigninController verifica si hay algún error presente en el Security context de Spring.
 * <p>
 * Cuando hay un error presente, registra el error usando el BindingResult. De esta manera, el mensaje de 
 * error estará disponible en la vista como cualquier otro mensaje de error. Tener en cuenta que 
 * este controlador se invoca sólo para mostrar la página de login y en caso de fallo de autenticación.
 * <p>
 * La fase de login en si es realizada por Spring Security.
 * <p>
 */

@Controller
@RequestMapping("/account")
public class SigninController {
	
	@RequestMapping(value = "signin", method = RequestMethod.GET)
    public String index(@ModelAttribute("loginForm") LoginForm loginForm,  BindingResult result, HttpServletRequest request) {
		AuthenticationException ae = (AuthenticationException) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (ae != null) {
            result.reject(ae.getClass().getName());
        }
        return "/account/login";
    }
	
	@RequestMapping(value="signout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "redirect:/account/signin.html";
	}

}
