package net.cloudengine.service.web;

import net.cloudengine.service.ConfigurationService;
import net.cloudengine.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

public class CustomFreeMarkerConfigurer extends FreeMarkerConfigurer {

	private ConfigurationService service;
	private SessionService sessionService;
	
	@Autowired
	public CustomFreeMarkerConfigurer(ConfigurationService service, SessionService sessionService) {
		super();
		this.service = service;
		this.sessionService = sessionService;
	}

	@Override
	public Configuration getConfiguration() {
		Configuration conf = super.getConfiguration();
		conf.addAutoImport("spring", "/spring.ftl");
		conf.addAutoImport("widget", "/widgets.ftl");
		conf.addAutoImport("form", "/form.ftl");
		conf.addAutoImport("page", "/page.ftl");
		conf.addAutoImport("layout", "/layouts/new_layout.ftl");
		try {
			conf.setSharedVariable("jnlpUrl", service.getProperty("jnlp.url").getValue());
			conf.setSharedVariable("appVersion", service.getVersion());
			conf.setSharedVariable("buildNumber", service.getBuildNumber());
			conf.setSharedVariable("osName", System.getProperty("os.name"));
			conf.setSharedVariable("cpuInfo", Runtime.getRuntime().availableProcessors());
			conf.setSharedVariable("javaVersion", System.getProperty("java.version"));
			conf.setSharedVariable("sessionService", sessionService);
			conf.setSharedVariable("Get", new GetPropertyTemplateMethod());
		} catch (Exception e) {
			e.printStackTrace();
		}
		conf.setTemplateExceptionHandler(new WebPageTemplateExceptionHandler());
		return conf;
	}
}
