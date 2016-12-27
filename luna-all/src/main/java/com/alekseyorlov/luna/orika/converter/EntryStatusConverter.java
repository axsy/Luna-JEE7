package com.alekseyorlov.luna.orika.converter;

import com.alekseyorlov.luna.domain.Entry;
import com.google.common.base.CaseFormat;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

@Component
public class EntryStatusConverter extends BidirectionalConverter<Entry.Status, String> {

    @Override
    public String convertTo(Entry.Status source, Type<String> destinationType) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, source.name());
    }

    @Override
    public Entry.Status convertFrom(String source, Type<Entry.Status> destinationType) {
        return Enum.valueOf(
                destinationType.getRawType(), CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, source));
    }
}
