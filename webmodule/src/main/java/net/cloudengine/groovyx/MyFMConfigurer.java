package net.cloudengine.groovyx;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

public class MyFMConfigurer extends FreeMarkerConfigurer {

	@Override
	public Configuration getConfiguration() {
		Configuration conf = super.getConfiguration();
		conf.addAutoImport("spring", "/spring.ftl");
		conf.addAutoImport("widget", "/widgets.ftl");
		conf.addAutoImport("layout", "/layouts/layout.ftl");
//		conf.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		return conf;
	}
	
	

}
