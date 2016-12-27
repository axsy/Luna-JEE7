package com.alekseyorlov.luna.domain.listener;

import com.alekseyorlov.luna.config.TestConfig;
import com.alekseyorlov.luna.domain.Element;
import com.alekseyorlov.luna.domain.Entry;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test", "integration"})
@Transactional
public class SlugListenerTest {

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

    @Test
    public void shouldSetSlugOnPersist() {

        // given
        Entry entry = createEntry("Some Title");

        // when
        entryRepository.save(entry);

        // then
        assertEquals("some-title", entry.getSlug());
    }

    private Entry createEntry(String title) {
        Entry entry = new Entry();

        entry.setOwner(userRepository.findOne(OWNER_ID));
        entry.setStatus(Entry.Status.DRAFT);
        entry.setTitle(title);
        entry.setType(entryTypeRepository.findOne(RAW_ENTRY_TYPE_ID));

        Element element = new Element();
        element.setType(elementTypeRepository.findOne(MARKDOWN_ELEMENT_TYPE_ID));

        Map<String, String> elementData = new HashMap<>();
        elementData.put("markup","Some markup");
        element.setData(elementData);

        entry.setElements(Arrays.asList(element));

        return entry;
    }
}