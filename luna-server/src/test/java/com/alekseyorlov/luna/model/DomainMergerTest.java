package com.alekseyorlov.luna.model;

import com.alekseyorlov.luna.Application;
import com.alekseyorlov.luna.service.DomainMerger;
import com.alekseyorlov.luna.service.DomainMerger.MergeStartegy;
import com.alekseyorlov.luna.model.repository.EntryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test", "integration"})
public class DomainMergerTest {

    @Autowired
    private DomainMerger merger;

    @Autowired
    private EntryRepository entryRepository;

    @Test
    public void shouldUpdateEntryBasicProperties() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final String newTitle = "Some new title";
        final Entry sourceEntry = new Entry();
        sourceEntry.setTitle(newTitle);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.UPDATE);

        // then
        assertEquals(newTitle, targetEntry.getTitle());
        assertNotNull(targetEntry.getSlug());
    }

    @Test
    public void shouldUpdateToManyAssociations() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final Entry sourceEntry = new Entry();

        final Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName("occaecat");

        final List<Taxonomy> taxonomies = Arrays.<Taxonomy>asList(taxonomy);
        sourceEntry.setTaxonomies(taxonomies);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.UPDATE);

        // then
        assertEquals(taxonomies, targetEntry.getTaxonomies());
        assertNotNull(targetEntry.getElements());
    }

    @Test
    public void shouldUpdateToOneAssociations() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final Entry sourceEntry = new Entry();

        final User user = new User();
        user.setUsername("new user");
        sourceEntry.setOwner(user);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.UPDATE);

        // then
        assertEquals(user, targetEntry.getOwner());
        assertNotNull(targetEntry.getElements());
    }

    @Test
    public void shouldPatchEntryBasicProperties() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final String newTitle = "Some new title";
        final Entry sourceEntry = new Entry();
        sourceEntry.setTitle(newTitle);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.PATCH);

        // then
        assertEquals(newTitle, targetEntry.getTitle());
        assertNull(targetEntry.getSlug());
    }

    @Test
    public void shouldPatchToManyAssociations() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final Entry sourceEntry = new Entry();

        final Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName("occaecat");

        final List<Taxonomy> taxonomies = Arrays.<Taxonomy>asList(taxonomy);
        sourceEntry.setTaxonomies(taxonomies);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.PATCH);

        // then
        assertEquals(taxonomies, targetEntry.getTaxonomies());
        assertNull(targetEntry.getElements());
    }

    @Test
    public void shouldPatchToOneAssociations() throws Exception {

        // given
        final Entry targetEntry = entryRepository.findOne(1L);

        final Entry sourceEntry = new Entry();

        final User user = new User();
        user.setUsername("new user");
        sourceEntry.setOwner(user);

        // when
        merger.<Entry>merge(targetEntry, sourceEntry, MergeStartegy.PATCH);

        // then
        assertEquals(user, targetEntry.getOwner());
        assertNull(targetEntry.getElements());
    }
}