package net.cloudengine.web.crud;

import org.springframework.web.servlet.ModelAndView;


public abstract class CrudController {
	
	protected static final String ID = "id";
	
	public abstract ModelAndView list(int total, int page);
	public abstract ModelAndView list();	
	
}

