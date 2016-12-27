package com.alekseyorlov.luna.orika.mapper.converter;

import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.orika.converter.EntryStatusConverter;
import ma.glasnost.orika.metadata.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumStringLowerCamelConverterTest {

    private EntryStatusConverter converter = new EntryStatusConverter();

    @Test
    public void shouldConvertEnumToString() throws Exception {

        // given
        Entry.Status status = Entry.Status.NOT_PUBLISHED;
        String lowerCamelStatus = "notPublished";

        // when
        String result = converter.convertTo(status, TypeFactory.valueOf(String.class));

        // then
        assertEquals(lowerCamelStatus, result);
    }

    @Test
    public void shouldConvertEnumFromString() throws Exception {

        // given
        String lowerCamelStatus = "timedPublish";
        Entry.Status status = Entry.Status.TIMED_PUBLISH;

        // when
        Entry.Status result = converter.convertFrom(lowerCamelStatus, TypeFactory.valueOf(Entry.Status.class));

        // then
        assertEquals(status, result);
    }
}