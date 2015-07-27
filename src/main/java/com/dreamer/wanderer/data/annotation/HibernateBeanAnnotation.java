package com.dreamer.wanderer.data.annotation;

import com.dreamer.wanderer.bo.Snap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rjain236 on 25/7/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in method only.
public @interface HibernateBeanAnnotation {
    public Class<? extends Snap> type();
    public String identifier();
    public boolean enabled() default true;
}
