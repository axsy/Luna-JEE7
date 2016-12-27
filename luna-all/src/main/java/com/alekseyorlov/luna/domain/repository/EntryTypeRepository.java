package com.alekseyorlov.luna.domain.repository;

import com.alekseyorlov.luna.domain.EntryType;
import org.springframework.data.repository.CrudRepository;

public interface EntryTypeRepository extends CrudRepository<EntryType, String> {
}
