package com.alekseyorlov.luna.service;

import com.alekseyorlov.luna.config.TestConfig;
import com.alekseyorlov.luna.domain.Entry.Status;
import com.alekseyorlov.luna.domain.Entry_;
import com.alekseyorlov.luna.domain.repository.EntryRepository;
import com.alekseyorlov.luna.dto.Entry;
import com.alekseyorlov.luna.dto.Patch;
import com.alekseyorlov.luna.dto.patch.Operation;
import com.alekseyorlov.luna.ejb.Entries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test", "integration"})
@Transactional
public class EntriesTest {

    @Autowired
    private Entries entries;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldFindEntry() throws Exception {

        // given
        Long entryId = 1L;

        // when
        Entry entryDto = entries.find(1L);

        // then
        assertEquals(entryId, entryDto.getId());
    }

    @Test
    public void shouldFindAllEntries() throws Exception {

        // given
        Integer pageSize = 1;
        Integer pageNumber = 1;
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        // when
        Page<Entry> entryDto = entries.findAll(pageRequest);

        // then
        assertNotNull(entryDto);
        assertEquals(1, entryDto.getTotalElements());
    }

    @Test
    public void shouldFindAllEntriesBySpecification() throws Exception {

        // given
        Integer pageSize = 1;
        Integer pageNumber = 1;
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        // when
        Page<Entry> entryDto = entries.findAll(TestSpecs.isPublished(), pageRequest);

        // then
        assertNotNull(entryDto);
        assertTrue(entryDto.getTotalElements() > 0);
    }

    @Test
    public void shouldSaveEntry() throws Exception {

        // given
        Entry newEntry = createEntry();

        // when
        Entry savedEntry = entries.save(newEntry);

        // then
        assertNotNull(savedEntry.getId());
    }

    @Test
    public void shouldPatchEntry() throws Exception {

        // given
        Long entryId = 1L;

        com.alekseyorlov.luna.domain.Entry entry = entryRepository.findOne(entryId);
        entityManager.detach(entry);

        Operation operation = new Operation();
        operation.setType(Operation.Type.ADD);
        operation.setPath("/taxonomies/tag/-");
        operation.setValue("new tag");

        Patch patch = new Patch();
        patch.setOperations(Arrays.asList(operation));

        // when
        entries.patch(entryId, patch);

        // then
        com.alekseyorlov.luna.domain.Entry patchedEntry = entryRepository.findOne(entryId);

        assertEquals(entry.getOwner(), patchedEntry.getOwner());
        assertEquals(entry.getStatus(), patchedEntry.getStatus());
        assertEquals(entry.getTitle(), patchedEntry.getTitle());
        assertEquals(entry.getSlug(), patchedEntry.getSlug());
        assertEquals(entry.getType(), patchedEntry.getType());
        assertEquals(entry.getElements(), patchedEntry.getElements());
        assertTrue(entry.getTaxonomies().size() < patchedEntry.getTaxonomies().size());
        assertEquals(entry.getCreatedAt(), patchedEntry.getCreatedAt());
        assertEquals(entry.getUpdatedAt(), patchedEntry.getUpdatedAt());
        assertEquals(entry.getPublishedAt(), patchedEntry.getPublishedAt());
        assertEquals(entry.getUnpublishedAt(), patchedEntry.getUnpublishedAt());
    }

    @Test
    public void shouldDeleteEntry() throws Exception {

        // given
        Long entryId = 1L;

        // when
        entries.delete(entryId);

        // then
        assertNull(entryRepository.findOne(entryId));
    }

    @Test
    public void shouldDeleteAll() throws Exception {

        // when
        entries.deleteAll();

        // then
        assertFalse(entryRepository.findAll().iterator().hasNext());
    }

    private Entry createEntry() {
        Entry entry = new Entry();
        entry.setStatus("published");
        entry.setTitle("some title");
        entry.setType("raw");

        Map<String, List<String>> taxonomies = new HashMap<>();
        taxonomies.put("tag", Arrays.asList("first tag", "second tag"));
        entry.setTaxonomies(taxonomies);

        Map<String, Map<String, String>> elements = new HashMap<>();
        Map<String, String> elementData = new HashMap<>();
        elementData.put("markup", "some markdown");
        elements.put("markdown", elementData);
        entry.setElements(elements);

        return entry;
    }

    private static class TestSpecs {

        public static Specification<com.alekseyorlov.luna.domain.Entry> isPublished() {
            return new Specification<com.alekseyorlov.luna.domain.Entry>() {

                @Override
                public Predicate toPredicate(
                        Root<com.alekseyorlov.luna.domain.Entry> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(Entry_.status), Status.PUBLISHED);
                }
            };
        }
    }
}