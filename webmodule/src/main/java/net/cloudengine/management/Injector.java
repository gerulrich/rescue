package net.cloudengine.management;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class Injector implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
	
	private static ApplicationContext applicationContext;
	private static boolean loaded = false;
	
	public static <T> T getDependency(Class<T> clazz) {
		if (applicationContext != null && loaded) {
			return applicationContext.getBean(clazz);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		Injector.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event instanceof ContextRefreshedEvent && getContextName(event).contains("springmvc-servlet")) {
			Injector.loaded = true;
		}
	}

	private String getContextName(ContextRefreshedEvent event) {
		return event.getApplicationContext().getDisplayName();
	}
}
