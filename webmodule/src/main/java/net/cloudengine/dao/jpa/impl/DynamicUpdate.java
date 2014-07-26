package net.cloudengine.dao.jpa.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;
import org.hibernate.annotations.Entity;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass({"net.cloudengine.dao.jpa.impl.DynamicUpdateTransformation"})
public @interface DynamicUpdate {
	
	Entity value() default @Entity(dynamicUpdate=true);

}
