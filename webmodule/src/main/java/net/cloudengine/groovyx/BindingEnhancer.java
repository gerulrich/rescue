package net.cloudengine.groovyx;

import groovy.lang.Binding;
import groovy.xml.MarkupBuilder;

import java.io.StringWriter;

import net.cloudengine.model.auth.User;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class BindingEnhancer {

	public static void bind(Binding binding) {

		StringWriter writer = new StringWriter();
		MarkupBuilder xml = new MarkupBuilder(writer);
		
		binding.setVariable("writer", writer);
		binding.setVariable("xml", xml);
		
		 SecurityContext ctx = SecurityContextHolder.getContext();
         Object principal = ctx.getAuthentication().getPrincipal();
         if ( principal instanceof User) {
        	 binding.setVariable("currentUser", principal);
         }

	}

}
