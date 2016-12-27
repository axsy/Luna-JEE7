package com.alekseyorlov.luna.domain.repository;

import com.alekseyorlov.luna.domain.Element;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ElementRepository extends CrudRepository<Element, Long>{

    List<Element> findByEntryIdAndTypeIdIn(Long entryId, Collection<String> typeIds);

}
