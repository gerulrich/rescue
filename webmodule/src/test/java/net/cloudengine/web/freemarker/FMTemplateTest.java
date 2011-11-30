package net.cloudengine.web.freemarker;

import org.junit.Test;

public class FMTemplateTest {

	@Test
	public void testProcessTemplateWithModel() throws Exception {
		
//		FileTemplateLoader ftl1 = new FileTemplateLoader(new File("src/main/webapp/WEB-INF/ftl"));
//		ClassTemplateLoader ctl = new ClassTemplateLoader(FMTemplateTest.class, ".");
//		TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ctl };
//		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
//		
//		final Configuration configuration = new Configuration();
//		configuration.setTemplateLoader(mtl);
//	    configuration.setObjectWrapper(new DefaultObjectWrapper());
//	    
//	    configuration.addAutoImport("spring", "/spring.ftl");
//	    configuration.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
//	    
//	    
//	    final Template template = configuration.getTemplate("account/login.xml");
//	    final Map<String, Object> model = new HashMap<String, Object>();
//	    model.put("contextPath", "http://localhost");
//	    
//	    /* populate model */
//	    template.process(model, new OutputStreamWriter(System.out));
	}

}
