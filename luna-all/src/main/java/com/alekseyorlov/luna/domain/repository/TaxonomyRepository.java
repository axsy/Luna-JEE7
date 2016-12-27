package com.alekseyorlov.luna.domain.repository;

import com.alekseyorlov.luna.domain.Taxonomy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface TaxonomyRepository extends CrudRepository<Taxonomy, Long>, JpaSpecificationExecutor {

    List<Taxonomy> findByTypeIdAndNameIn(String id, Collection<String> name);

}
