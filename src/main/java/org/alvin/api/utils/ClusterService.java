package org.alvin.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClusterService {

	String TYPE_ALIYUN = "aliyun";
	String TYPE_QCLOUD = "qcloud";
	String TYPE_UCLOUD = "ucloud";
	String TYPE_GYUN = "gyun";

	String value() default "";

}
