package com.alekseyorlov.luna.orika.converter;

import com.alekseyorlov.luna.domain.Taxonomy;
import com.alekseyorlov.luna.domain.TaxonomyType;
import com.alekseyorlov.luna.domain.repository.TaxonomyRepository;
import com.alekseyorlov.luna.domain.repository.TaxonomyTypeRepository;
import com.google.common.collect.Sets;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EntryTaxonomiesConverter extends BidirectionalConverter<List<Taxonomy>, Map<String, List<String>>> {

    @Autowired
    private TaxonomyRepository repository;

    @Autowired
    private TaxonomyTypeRepository typeRepository;

    @Override
    public Map<String, List<String>> convertTo(List<Taxonomy> source, Type<Map<String, List<String>>> destinationType) {
        Map<String, List<String>> taxonomies = new HashMap<>();

        for(Taxonomy taxonomy: source) {
            String key = taxonomy.getType().getId();
            if (!taxonomies.containsKey(key)) {
                taxonomies.put(key, new ArrayList<>());
            }

            taxonomies.get(key).add(taxonomy.getName());
        }

        return taxonomies;
    }

    @Override
    public List<Taxonomy> convertFrom(Map<String, List<String>> source, Type<List<Taxonomy>> destinationType) {
        List<Taxonomy> taxonomies = new ArrayList<>();

        for (String taxonomyKey: source.keySet()) {
            List<Taxonomy> taxonomyGroup = repository.findByTypeIdAndNameIn(taxonomyKey, source.get(taxonomyKey));

            // There are new taxonomies to be added
            if (taxonomyGroup.size() != source.get(taxonomyKey).size()) {
                List<String> persistedTaxonomies = new ArrayList<>();
                for (Taxonomy taxonomy: taxonomyGroup) {
                    persistedTaxonomies.add(taxonomy.getName());
                }

                Sets.SetView<String> newTaxonomyNames = Sets.difference(
                        new HashSet<>(source.get(taxonomyKey)), new HashSet<>(persistedTaxonomies));
                TaxonomyType taxonomyType = typeRepository.findOne(taxonomyKey);
                for (String newTaxonomyName: newTaxonomyNames) {
                    Taxonomy taxonomy = new Taxonomy();

                    taxonomy.setType(taxonomyType);
                    taxonomy.setName(newTaxonomyName);

                    taxonomyGroup.add(taxonomy);
                }

            }

            taxonomies.addAll(taxonomyGroup);
        }

        return taxonomies;
    }
}
