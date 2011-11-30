package net.cloudengine.groovyx;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.View;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

public class GroovyView implements View {

	private Resource resource;

	public GroovyView(Resource resource) {
		super();
		this.resource = resource;
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contextPath", request.getContextPath());
		params.put("currentPage", resource.getFilename());
		
		
		params.putAll(model);
		
		FileTemplateLoader ftl1 = new FileTemplateLoader(new File(resource.getFile().getParent()));
		FileTemplateLoader ftl2 = new FileTemplateLoader(new File("src/main/webapp/WEB-INF/ftl"));
		TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2 };
		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);

		Configuration cfg = new Configuration();
		cfg.setTemplateLoader(mtl);
		
		freemarker.template.Template tpl = cfg.getTemplate(resource.getFilename());
		tpl.process(params, response.getWriter());
		
//		Binding binding = new Binding();
//		BindingEnhancer.bind(binding);

//		String[] roots = new String[] { resource.getFile().getParent() };
//		GroovyScriptEngine gse = new GroovyScriptEngine(roots);
		
//		XmlTemplateEngine gse = new XmlTemplateEngine();
//		Template template = gse.createTemplate(resource.getFile());
		
//		response.setContentType(getContentType());
		
//		template.make(binding.getVariables()).writeTo(response.getWriter());

		
//		response.getWriter().append(result.toString());

	}

}