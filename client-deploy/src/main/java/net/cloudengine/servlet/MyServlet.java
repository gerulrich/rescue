package net.cloudengine.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String URL_PROPERTY = "url";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		Writer w = resp.getWriter();
		
		Properties p = new Properties();
		p.load(MyServlet.class.getResourceAsStream("config.properties"));
		overrideProperties(p);
		for (Object s : p.keySet()) {
			String key = s.toString();
			String value= p.getProperty(key);
			w.write(key+"="+value+"\n");
		}
		w.close();
		
	}
	
	private void overrideProperties(Properties p) {
		Map<String, String> env = System.getenv();
		if (env.get(URL_PROPERTY)!=null) {
			p.put(URL_PROPERTY, env.get(URL_PROPERTY));
		}
	}

}
