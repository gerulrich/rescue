package net.cloudengine.client.ui;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( { METHOD })
@Retention(RUNTIME)
public @interface PostCallback {

	String name();
}
