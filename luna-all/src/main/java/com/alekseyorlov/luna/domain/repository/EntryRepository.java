package com.alekseyorlov.luna.domain.repository;

import com.alekseyorlov.luna.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface EntryRepository extends JpaRepository<Entry, Long>, JpaSpecificationExecutor {
}
