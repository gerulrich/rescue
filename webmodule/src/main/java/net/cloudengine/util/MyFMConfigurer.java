package net.cloudengine.util;

import net.cloudengine.service.admin.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

public class MyFMConfigurer extends FreeMarkerConfigurer {

	private ConfigurationService service;
	
	@Autowired
	public MyFMConfigurer(ConfigurationService service) {
		super();
		this.service = service;
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
			conf.setSharedVariable("osName", System.getProperty("os.name"));
			conf.setSharedVariable("javaVersion", System.getProperty("java.version"));
			conf.setSharedVariable("userUtil", UserUtil.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		conf.setTemplateExceptionHandler(new MyTemplateExceptionHandler());
		return conf;
	}
}
