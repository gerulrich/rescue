package net.cloudengine.freemarker;

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
		conf.addAutoImport("layout", "/layouts/layout.ftl");
		try {
			conf.setSharedVariable("jnlpUrl", service.getProperty("jnlp.url").getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		conf.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		return conf;
	}
	
	

}
