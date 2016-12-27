package com.alekseyorlov.luna.orika.configurer;

import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class NullMapperConfigurer implements MapperConfigurer, ApplicationContextAware {

    protected ApplicationContext context;

    @Override
    public void configure(MapperFactory factory) {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
