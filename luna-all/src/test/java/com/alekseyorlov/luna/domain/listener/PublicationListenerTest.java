package com.alekseyorlov.luna.domain.listener;

import com.alekseyorlov.luna.config.TestConfig;
import com.alekseyorlov.luna.domain.Element;
import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.domain.Entry.Status;
import com.alekseyorlov.luna.domain.repository.ElementTypeRepository;
import com.alekseyorlov.luna.domain.repository.EntryRepository;
import com.alekseyorlov.luna.domain.repository.EntryTypeRepository;
import com.alekseyorlov.luna.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test", "integration"})
@Transactional
public class PublicationListenerTest {

    private static final Long OWNER_ID = 1L;
    private static final String RAW_ENTRY_TYPE_ID = "raw";
    private static final String MARKDOWN_ELEMENT_TYPE_ID = "markdown";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EntryTypeRepository entryTypeRepository;

    @Autowired
    private ElementTypeRepository elementTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldNotSetPublished() {

        // given
        Entry entry = createEntry(Status.DRAFT);

        // when
        entryRepository.save(entry);

        // then
        assertNull(entry.getPublishedAt());
    }

    @Test
    public void shouldSetPublishedAtOnInstantPublication() {

        // given
        Entry entry = createEntry(Status.PUBLISHED);

        // when
        entryRepository.save(entry);

        // then
        assertNotNull(entry.getPublishedAt());
    }

    // http://stackoverflow.com/questions/24338150/how-to-manually-force-a-commit-in-a-transactional-method/24341843#24341843
    @Test
    public void shouldSetPublishedAtOnPublication() {

        // given
        Entry entry = createEntry(Status.DRAFT);
        entry = entryRepository.save(entry);

        entry.setStatus(Status.PUBLISHED);

        // when
        entry = entryRepository.save(entry);
        // entryRepository.save() performs EntityManager.merge() in case the entity is already persisted.
        // Because transaction is not commited yet and flush mode is AUTO there is the necessity
        // to flush changes manually in order to trigger JPA listeners
        // More on flushing: http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#__code_auto_code_flush_on_commit
        entityManager.flush();
        // then
        assertNotNull(entry.getPublishedAt());
    }

    // http://stackoverflow.com/questions/24338150/how-to-manually-force-a-commit-in-a-transactional-method/24341843#24341843
    @Test
    public void shouldSetUnpublishedAtOnUnpublication() {

        // given
        Entry entry = createEntry(Status.PUBLISHED);
        entry = entryRepository.save(entry);

        entry.setStatus(Status.NOT_PUBLISHED);

        // when
        entry = entryRepository.save(entry);
        // entryRepository.save() performs EntityManager.merge() in case the entity is already persisted.
        // Because transaction is not commited yet and flush mode is AUTO there is the necessity
        // to flush changes manually in order to trigger JPA listeners
        // More on flushing: http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#__code_auto_code_flush_on_commit
        entityManager.flush();
        // then
        assertNotNull(entry.getUnpublishedAt());
    }

    private Entry createEntry(Status status) {
        Entry entry = new Entry();

        entry.setOwner(userRepository.findOne(OWNER_ID));
        entry.setStatus(status);
        entry.setTitle("some title");
        entry.setType(entryTypeRepository.findOne(RAW_ENTRY_TYPE_ID));

        Element element = new Element();
        element.setType(elementTypeRepository.findOne(MARKDOWN_ELEMENT_TYPE_ID));

        Map<String, String> elementData = new HashMap<>();
        elementData.put("markup","Some markup");
        element.setData(elementData);

        ArrayList<Element> elements = new ArrayList<>();
        elements.add(element);
        entry.setElements(elements);

        return entry;
    }
}