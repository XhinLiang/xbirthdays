package com.xhinliang.birthdays.runner;

import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.base.Preconditions;

/**
 * @author xhinliang
 */
public abstract class BaseApplicationRunner implements ApplicationRunner, Ordered {

    @Override
    public int getOrder() {
        ApplicationRunnerOrder order = AnnotationUtils.findAnnotation(this.getClass(), ApplicationRunnerOrder.class);
        Preconditions.checkNotNull(order, "%s has not register in ApplicationRunnerConfig", this.getClass().toGenericString());
        return order.value();
    }
}
