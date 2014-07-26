package net.cloudengine.web.crud.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CrudProperty {
	
	boolean editable() default true;
	boolean show() default true;
	boolean embedded() default false;
	String key() default "";
	
}
