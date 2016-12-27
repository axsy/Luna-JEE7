package com.alekseyorlov.luna.domain.listener;

import com.alekseyorlov.luna.domain.listener.annotation.Slug;
import com.alekseyorlov.luna.domain.listener.support.SlugGenerationStrategy;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class SlugListener {

    @PreUpdate
    @PrePersist
    public void setSlug(Object entity) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<? extends Object> entityClass = entity.getClass();
        Field[] entityFields = entityClass.getDeclaredFields();
        for (Field targetField: entityFields) {
            Slug annotation = targetField.getAnnotation(Slug.class);
            if (annotation != null) {
                Class<?> sluggingStrategyClass = annotation.strategy();
                if (!hasParameterlessConstructor(sluggingStrategyClass)) {
                    throw new RuntimeException("Slug generation strategy class has no default constructor");
                }

                Object sluggingStrategy = sluggingStrategyClass.newInstance();
                if (!(sluggingStrategy instanceof SlugGenerationStrategy)) {
                    throw new RuntimeException("Slug generation strategy does not implement " +
                            "com.alekseyorlov.luna.domain.listener.annotation.support.SlugGenerationStrategy");
                }

                Field sourceField = entityClass.getDeclaredField(annotation.source());
                sourceField.setAccessible(true);
                String slug = ((SlugGenerationStrategy)sluggingStrategy).generate(sourceField.get(entity).toString());

                targetField.setAccessible(true);
                targetField.set(entity, slug);
            }
        }
    }

    private boolean hasParameterlessConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }
}
