package com.alekseyorlov.luna.ejb;

import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.domain.repository.EntryRepository;
import com.alekseyorlov.luna.dto.Patch;
import com.alekseyorlov.luna.util.DomainObjectMerger;
import com.alekseyorlov.luna.util.DomainObjectMerger.MergeStartegy;
import com.alekseyorlov.luna.util.ObjectPatcher;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Entries {

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private DomainObjectMerger merger;

    @Autowired
    private ObjectPatcher patcher;

    @Autowired
    private EntryRepository repository;

    @Transactional
    public com.alekseyorlov.luna.dto.Entry find(Long id) {
        Entry entry = repository.findOne(id);
        com.alekseyorlov.luna.dto.Entry entryDTO = null;
        if (entry != null) {
            entryDTO = mapper.map(entry, com.alekseyorlov.luna.dto.Entry.class);
        }

        return entryDTO;
    }

    @Transactional
    public Page<com.alekseyorlov.luna.dto.Entry> findAll(Pageable pageRequest) {
        Page<Entry> entryPage = repository.findAll(pageRequest);

        return createDTOPage(entryPage, pageRequest);
    }

    @Transactional
    public Page<com.alekseyorlov.luna.dto.Entry> findAll(Specification specification, Pageable pageRequest) {
        Page<Entry> entryPage = repository.findAll(specification, pageRequest);

        return createDTOPage(entryPage, pageRequest);
    }

    @Transactional
    public com.alekseyorlov.luna.dto.Entry save(com.alekseyorlov.luna.dto.Entry newEntryDto) {
        Entry newEntry = mapper.map(newEntryDto, Entry.class);
        Entry savedEntry = repository.save(newEntry);

        return mapper.map(savedEntry, com.alekseyorlov.luna.dto.Entry.class);
    }

    @Transactional
    public void patch(Long entryId, Patch patch) {
        Entry entry = repository.findOne(entryId);
        com.alekseyorlov.luna.dto.Entry entryDto = mapper.map(entry, com.alekseyorlov.luna.dto.Entry.class);

        try {
            entryDto = patcher.patch(
                    entryDto,
                    patch,
                    com.alekseyorlov.luna.dto.Entry.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO: Is there any sense in different merging strategies in the view of RFC6902? To be researched.
        merger.merge(entry, mapper.map(entryDto, Entry.class), MergeStartegy.PATCH);
        repository.save(entry);
    }

    @Transactional
    public void delete(Long entryId) {
        repository.delete(entryId);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    private Page<com.alekseyorlov.luna.dto.Entry> createDTOPage(Page<Entry> entryPage, Pageable pageRequest) {
        List<com.alekseyorlov.luna.dto.Entry> dtoList = new ArrayList<>();
        for (Entry entry: entryPage.getContent()) {
            if (entry != null) {
                dtoList.add(mapper.map(entry, com.alekseyorlov.luna.dto.Entry.class));
            }
        }

        return new PageImpl<com.alekseyorlov.luna.dto.Entry>(dtoList, pageRequest, entryPage.getTotalElements());
    }
}
