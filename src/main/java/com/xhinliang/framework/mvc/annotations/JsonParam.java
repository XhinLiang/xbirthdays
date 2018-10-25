package com.xhinliang.framework.mvc.annotations;

import java.lang.annotation.*;

/**
 * @author xhinliang
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {

    String DEFAULT_HOLDER = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";

    String value() default "";

    boolean required() default true;

    String defaultValue() default DEFAULT_HOLDER;
}
