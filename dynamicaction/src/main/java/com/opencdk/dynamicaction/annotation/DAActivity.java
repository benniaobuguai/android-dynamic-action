package com.opencdk.dynamicaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DAActivity {

	public String name() default "<NULL>";

	public String desc() default "<NULL>";

	public String version() default "";

}
