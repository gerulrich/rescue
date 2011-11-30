package net.cloudengine.groovyx;

import java.util.Locale;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class GroovyViewResolver implements ViewResolver, ResourceLoaderAware {

	private String prefix, suffix;

	private ResourceLoader resourceLoader;

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		
		Resource resource = resourceLoader.getResource(prefix + viewName+ suffix);
		if (resource.exists()) {
			return new GroovyView(resource);
		} else {
			return null;
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader rl) {
		this.resourceLoader = rl;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}