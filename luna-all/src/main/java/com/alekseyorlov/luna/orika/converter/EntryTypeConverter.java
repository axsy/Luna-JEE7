package com.alekseyorlov.luna.orika.converter;

import com.alekseyorlov.luna.domain.EntryType;
import com.alekseyorlov.luna.domain.repository.EntryTypeRepository;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntryTypeConverter extends BidirectionalConverter<EntryType, String> {

    @Autowired
    private EntryTypeRepository repository;

    @Override
    public String convertTo(EntryType source, Type<String> destinationType) {
        return source.getId();
    }

    @Override
    public EntryType convertFrom(String source, Type<EntryType> destinationType) {
        return repository.findOne(source);
    }
}
