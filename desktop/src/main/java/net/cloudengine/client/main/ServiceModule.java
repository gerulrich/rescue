package net.cloudengine.client.main;

import java.net.MalformedURLException;

import net.cloudengine.client.service.Configuration;
import net.cloudengine.client.service.ConfigurationImpl;
import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.SigninService;
import net.cloudengine.rpc.controller.config.ContextImpl;
import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.rpc.controller.geo.ZoneController;
import net.cloudengine.rpc.controller.resource.ResourceController;
import net.cloudengine.util.UncheckedThrow;

import com.caucho.hessian.client.MyHessianProxyFactory;
import com.caucho.services.client.ServiceProxyFactory;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	private static final Context context = new ContextImpl();
	
	@Override
	protected void configure() {
		try {
			
			Configuration conf = new ConfigurationImpl();
			String baseUrl = conf.getBaseUrl();
			
			MyHessianProxyFactory factory = new MyHessianProxyFactory();
			bind(Configuration.class).toInstance(conf);
			bind(Context.class).toInstance(context);
			bind(ServiceProxyFactory.class).toInstance(factory);
			bindService(baseUrl, SigninService.class, factory);
			bindService(baseUrl, PropertyController.class, factory);
			bindService(baseUrl, ResourceController.class, factory);
			bindService(baseUrl, ZoneController.class, factory);
			
		} catch (Exception e) {
			UncheckedThrow.throwUnchecked(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> void bindService(String baseUrl, Class<T> clazz, MyHessianProxyFactory factory) {
		try {
			T service = (T) factory.create(clazz, baseUrl+"hessian/"+clazz.getSimpleName());
			bind(clazz).toInstance(service);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}