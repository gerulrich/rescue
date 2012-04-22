package net.cloudengine.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		Writer w = resp.getWriter();
		
		Properties p = new Properties();
		p.load(MyServlet.class.getResourceAsStream("config.properties"));
		
		for (Object s : p.keySet()) {
			String key = s.toString();
			String value= p.getProperty(key);
			w.write(key+"="+value+"\n");
		}
		w.close();
		
	}
	
	
	
	

}
