package com.alekseyorlov.luna.util;

import java.util.Collection;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.*;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Does not remove @ManyToOne and @OneToOne associations
 */
@Component
public class DomainObjectMerger {

    public enum MergeStartegy {

        /**
         * Applies nulls
         */
        UPDATE,

        /**
         * Skips nulls
         */
        PATCH
    }

    @Autowired
    private Repositories repositories;

    public <Type> Type merge(Type target, Type source, MergeStartegy mergeStartegy) {
    	
    	// TODO: REPLACE WITH APACHE COMMONS / BEAN UTILS - http://commons.apache.org/proper/commons-beanutils/ | http://stackoverflow.com/questions/5856895/java-reflection-beans-property-api
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);
        BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
        PersistentEntity<?, ?> entity = repositories.getPersistentEntity(target.getClass());

        entity.doWithProperties(new SimplePropertyHandler() {

            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                Object sourceVal = sourceWrapper.getPropertyValue(property.getName());
                Object targetVal = targetWrapper.getPropertyValue(property.getName());
                if (!property.isIdProperty() && !ObjectUtils.nullSafeEquals(sourceVal,targetVal)
                    && ((mergeStartegy == MergeStartegy.PATCH && sourceVal != null)
                    || mergeStartegy == MergeStartegy.UPDATE)) {
                    targetWrapper.setPropertyValue(property.getName(), sourceVal);
                }
            }
        });

        entity.doWithAssociations(new SimpleAssociationHandler() {

            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                PersistentProperty<?> property = association.getInverse();
                Object sourceVal = sourceWrapper.getPropertyValue(property.getName());
                Object targetVal = targetWrapper.getPropertyValue(property.getName());

                if (sourceVal != null) {
                    if (sourceVal instanceof Collection && targetVal instanceof Collection) {
                        Collection persistentCollection = (Collection) targetVal;
                        persistentCollection.clear();
                        persistentCollection.addAll((Collection)sourceVal);
                    } else {
                        if (isNotEmpty(sourceVal) && !ObjectUtils.nullSafeEquals(sourceVal,targetVal)) {
                            targetWrapper.setPropertyValue(property.getName(), sourceVal);
                        }
                    }
                }
            }
        });

        return target;
    }

    private static boolean isNotEmpty(Object obj) {
        boolean result = true;
        if (obj instanceof Iterable<?>) {
            result = ((Iterable<?>)obj).iterator().hasNext();
        } else if (ObjectUtils.isArray(obj)) {
            result = (ObjectUtils.isEmpty((Object[]) obj));
        }

        return result;
    }
}
