package com.alekseyorlov.luna.orika;

import com.alekseyorlov.luna.orika.configurer.MapperConfigurer;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringConfigurableMapper extends ConfigurableMapper implements ApplicationContextAware {

    private ApplicationContext context;
    private MapperFactory factory;

    public SpringConfigurableMapper() {
        super(false);
    }

    @Override
    protected void configure(MapperFactory factory) {
        this.factory = factory;
        Map<String, MapperConfigurer> configurerMap = context.getBeansOfType(MapperConfigurer.class);
        for (MapperConfigurer configurer: configurerMap.values()) {
            configurer.configure(factory);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        init();
    }
}
