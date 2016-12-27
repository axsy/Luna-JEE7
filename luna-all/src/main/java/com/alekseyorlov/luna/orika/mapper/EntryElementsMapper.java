package com.alekseyorlov.luna.orika.mapper;

import com.alekseyorlov.luna.domain.Element;
import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.domain.repository.ElementRepository;
import com.alekseyorlov.luna.domain.repository.ElementTypeRepository;
import com.google.common.collect.Sets;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EntryElementsMapper extends CustomMapper<Entry, com.alekseyorlov.luna.dto.Entry> {

    @Autowired
    private ElementRepository repository;

    @Autowired
    private ElementTypeRepository typeRepository;

    @Override
    public void mapAtoB(Entry source, com.alekseyorlov.luna.dto.Entry destination, MappingContext context) {
        if (source.getElements() != null) {
            destination.setElements(new HashMap<>());
            for (Element element : source.getElements()) {
                destination.getElements().put(element.getType().getId(), element.getData());
            }
        }
    }

    @Override
    public void mapBtoA(com.alekseyorlov.luna.dto.Entry source, Entry destination, MappingContext context) {
        if (source.getElements() != null) {
            List<Element> elements = new ArrayList<>();
            if (source.getId() != null) {
                List<Element> elementsGroup = repository.findByEntryIdAndTypeIdIn(
                        source.getId(), source.getElements().keySet());

                if (elementsGroup.size() != source.getElements().size()) {
                    List<String> persistedElementTypes = new ArrayList<>();
                    for (Element element : elementsGroup) {
                        persistedElementTypes.add(element.getType().getId());
                    }

                    Sets.SetView<String> newElementTypes = Sets.difference(
                            new HashSet<>(source.getElements().keySet()), new HashSet<>(persistedElementTypes));
                    elementsGroup.addAll(createElementsForTypes(newElementTypes, source.getElements()));

                }
                elements.addAll(elementsGroup);
            } else {
                elements.addAll(createElementsForTypes(source.getElements().keySet(), source.getElements()));
            }

            destination.setElements(elements);
        }
    }

    private List<Element> createElementsForTypes(
            Collection<String> elementTypes, Map<String, Map<String, String>> elementsMap) {
        List<Element> elements = new ArrayList<>();
        for (String elementType: elementTypes) {
            Element element = new Element();

            element.setType(typeRepository.findOne(elementType));
            element.setData(elementsMap.get(elementType));

            elements.add(element);
        }

        return elements;
    }
}
