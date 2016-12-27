package com.alekseyorlov.luna.orika;

import com.alekseyorlov.luna.config.TestConfig;
import com.alekseyorlov.luna.domain.*;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class OrikaClassMapTest {

    @Autowired
    private MapperFacade mapperFacade;

    @Test
    public void shouldMapIdFromAToB() {

        // given
        Entry entry = new Entry();

        entry.setId(Long.MAX_VALUE);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertNotNull(entryDTO.getId());
    }

    @Test
    public void shouldMapTitleFromAToB() {

        // given
        Entry entry = new Entry();

        String title = "Some title";
        entry.setTitle(title);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(title, entryDTO.getTitle());
    }

    @Test
    public void shouldMapSlugFromAToB() {

        // given
        Entry entry = new Entry();

        String slug = "some-title";
        entry.setSlug(slug);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(slug, entryDTO.getSlug());
    }

    @Test
    public void shouldMapCreatedAtFromAToB() {

        // given
        Entry entry = new Entry();

        Instant instant = Instant.MAX;
        entry.setCreatedAt(instant);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(instant, entryDTO.getCreatedAt());
    }

    @Test
    public void shouldMapUpdatedAtFromAToB() {

        // given
        Entry entry = new Entry();

        Instant instant = Instant.MAX;
        entry.setUpdatedAt(instant);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(instant, entryDTO.getUpdatedAt());
    }

    @Test
    public void shouldMapPublishedAtFromAToB() {

        // given
        Entry entry = new Entry();

        Instant instant = Instant.MAX;
        entry.setPublishedAt(instant);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(instant, entryDTO.getPublishedAt());
    }

    @Test
    public void shouldMapUnpublishedAtFromAToB() {

        // given
        Entry entry = new Entry();

        Instant instant = Instant.MAX;
        entry.setUnpublishedAt(instant);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(instant, entryDTO.getUnpublishedAt());
    }

    @Test
    public void shouldMapTypeFromAToB() {

        // given
        String entryTypeId = "entryType";
        EntryType entryType = new EntryType();
        entryType.setId(entryTypeId);

        Entry entry = new Entry();
        entry.setType(entryType);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals(entryTypeId, entryDTO.getType());
    }

    @Test
    public void shouldMapTaxonomiesFromAToB() {

        // given
        TaxonomyType taxonomyType = new TaxonomyType();
        taxonomyType.setId("taxonomyType");

        Taxonomy firstTaxonomy = new Taxonomy();
        firstTaxonomy.setName("first");
        firstTaxonomy.setType(taxonomyType);

        Taxonomy secondTaxonomy = new Taxonomy();
        secondTaxonomy.setName("second");
        secondTaxonomy.setType(taxonomyType);

        Entry entry = new Entry();
        entry.setTaxonomies(Arrays.asList(firstTaxonomy, secondTaxonomy));

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        Map<String, List<String>> taxonomies = new HashMap<>();
        taxonomies.put("taxonomyType", Arrays.asList("first", "second"));

        assertEquals(taxonomies, entryDTO.getTaxonomies());
    }

    @Test
    public void shouldMapElementsFromAToB() {

        // given
        Element firstElement = new Element();

        ElementType firstElementType = new ElementType();
        firstElementType.setId("firstElementType");
        firstElement.setType(firstElementType);

        Map<String, String> firstElementData = new HashMap<>();
        firstElementData.put("key1", "value1");
        firstElementData.put("key2", "value2");
        firstElement.setData(firstElementData);

        Element secondElement = new Element();

        ElementType secondElementType = new ElementType();
        secondElementType.setId("secondElementType");
        secondElement.setType(secondElementType);

        Map<String, String> secondElementData = new HashMap<>();
        secondElementData.put("key1", "value1");
        secondElementData.put("key2", "value2");
        secondElement.setData(secondElementData);

        Entry entry = new Entry();
        entry.setElements(Arrays.asList(firstElement, secondElement));

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        Map<String, Map<String, String>> elements = new HashMap<>();
        elements.put("firstElementType", firstElementData);
        elements.put("secondElementType", secondElementData);

        assertEquals(elements, entryDTO.getElements());
    }

    // TODO: Write tests for BtoA mapping direction

    @Test
    public void shouldMapStatusFromAToB() {

        // given
        Entry entry = new Entry();
        entry.setStatus(Entry.Status.PUBLISHED);

        // when
        com.alekseyorlov.luna.dto.Entry entryDTO = mapperFacade.map(
                entry, com.alekseyorlov.luna.dto.Entry.class);

        // then
        assertEquals("published", entryDTO.getStatus());
    }

    @Test
    public void shouldNotMapIdFromBToA() {

        // given
        com.alekseyorlov.luna.dto.Entry entryDTO = new com.alekseyorlov.luna.dto.Entry();

        entryDTO.setId(Long.MAX_VALUE);

        // when
        Entry entry = mapperFacade.map(entryDTO, Entry.class);

        // then
        assertNull(entry.getId());
    }
}