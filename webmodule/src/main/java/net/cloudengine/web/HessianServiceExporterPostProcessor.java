package net.cloudengine.web;

import java.util.Map;

import net.cloudengine.util.ExternalService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Service;

/**
 * Exporta los servicios con la anotación {@link ExternalService} como servicios de Hessian.
 * @author German Ulrich
 *
 */
public class HessianServiceExporterPostProcessor implements BeanFactoryPostProcessor {

	private boolean isExternalService(Object bean) {
		return bean.getClass().getAnnotation(Service.class) != null && bean.getClass().getAnnotation(ExternalService.class) != null;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		Map<String, Object> beans = ((ConfigurableListableBeanFactory)factory.getParentBeanFactory()).getBeansWithAnnotation(ExternalService.class);		
		
		 for (String beanName : beans.keySet()) {
			 Object bean = beans.get(beanName);
			 if (isExternalService(bean)) {
				 ExternalService annotation = bean.getClass().getAnnotation(ExternalService.class);
				 Class<?> exportedInterface = annotation.exportedInterface();
				 BeanDefinitionRegistry registry = ((BeanDefinitionRegistry ) factory);
				 GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
				 beanDefinition.setBeanClass(HessianServiceExporter.class);
				 beanDefinition.setLazyInit(false);
				 beanDefinition.setAbstract(false);
				 beanDefinition.setAutowireCandidate(true);
				 beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
				 beanDefinition.getPropertyValues().addPropertyValue("service", factory.getBean(beanName));
				 beanDefinition.getPropertyValues().addPropertyValue("serviceInterface", exportedInterface);
				 registry.registerBeanDefinition("/hessian/"+exportedInterface.getSimpleName(), beanDefinition);
			 }
		 }
	}
}