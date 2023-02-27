package io.mcc.mobile.common.config.oauth2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DigestAnnotation {
	String value() default "";
	String serviceName() default "";
	String item_seq() default "";
}
