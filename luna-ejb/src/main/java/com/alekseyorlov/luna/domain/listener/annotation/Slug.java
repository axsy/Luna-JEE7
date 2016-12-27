package com.alekseyorlov.luna.domain.listener.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alekseyorlov.luna.domain.listener.support.DefaultSlugGenerationStrategy;

@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Slug {

    String source();

    Class<?> strategy() default DefaultSlugGenerationStrategy.class;

}
