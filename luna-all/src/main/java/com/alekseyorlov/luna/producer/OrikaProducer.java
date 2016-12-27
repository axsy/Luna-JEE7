package com.alekseyorlov.luna.producer;

import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.orika.converter.EntryTypeConverter;
import com.alekseyorlov.luna.orika.converter.EntryStatusConverter;
import com.alekseyorlov.luna.orika.converter.EntryTaxonomiesConverter;
import com.alekseyorlov.luna.orika.mapper.EntryElementsMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.time.Instant;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class OrikaProducer {

    private final static String ENUMERATION_CONVERTER_ID = "enumerationStringConverter";
    private final static String TAXONOMIES_CONVERTER_ID = "taxonomiesConverter";
    private final static String ENTRY_TYPE_CONVERTER_ID = "entryTypeConverter";

    @Inject
    private EntryElementsMapper mapper;

    @Inject
    private EntryStatusConverter statusConverter;

    @Inject
    private EntryTaxonomiesConverter taxonomiesConverter;

    @Inject
    private EntryTypeConverter entryTypeConverter;

    @Produces
    public MapperFactory mapperFactory() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
    	
        registerCustomConverters(factory);

        factory.classMap(Entry.class, com.alekseyorlov.luna.dto.Entry.class)
                .fieldAToB("id", "id")
                .field("title", "title")
                .field("slug", "slug")
                .field("createdAt", "createdAt")
                .field("updatedAt", "updatedAt")
                .field("publishedAt", "publishedAt")
                .field("unpublishedAt", "unpublishedAt")
                .fieldMap("type", "type")
                    .converter(ENTRY_TYPE_CONVERTER_ID)
                    .add()
                .fieldMap("status", "status")
                    .converter(ENUMERATION_CONVERTER_ID)
                    .add()
                .fieldMap("taxonomies", "taxonomies")
                    .converter(TAXONOMIES_CONVERTER_ID)
                    .add()
                .customize(mapper)
                .register();
        
        return factory;
    }

    private void registerCustomConverters(MapperFactory factory) {
        factory.getConverterFactory().registerConverter(new PassThroughConverter(Instant.class));

        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(
                ENUMERATION_CONVERTER_ID, statusConverter
        );
        converterFactory.registerConverter(
                TAXONOMIES_CONVERTER_ID, taxonomiesConverter
        );
        converterFactory.registerConverter(
                ENTRY_TYPE_CONVERTER_ID, entryTypeConverter
        );
    }
}
