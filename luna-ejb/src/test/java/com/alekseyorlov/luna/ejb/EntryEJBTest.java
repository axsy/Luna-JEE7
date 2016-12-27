package com.alekseyorlov.luna.ejb;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alekseyorlov.luna.domain.Element;
import com.alekseyorlov.luna.domain.ElementType;
import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.domain.Entry.Status;
import com.alekseyorlov.luna.domain.EntryType;
import com.alekseyorlov.luna.domain.User;
import com.alekseyorlov.luna.ejb.domain.Page;
import com.alekseyorlov.luna.ejb.domain.Pageable;
import com.alekseyorlov.luna.ejb.exception.NoItemFoundException;

@RunWith(Arquillian.class)
public class EntryEJBTest {

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        
        return create(EnterpriseArchive.class, "luna-test.ear")
                .setApplicationXML("META-INF/test-application.xml")
                
                // EJB to be tested
                .addAsModule(create(JavaArchive.class, "ejb.jar")

                        // EJBs
                        .addClass(EntryEJB.class)
                        .addClass(DomainEntry.class)
                        .addClass(GenericEntry.class)
                        .addClass(NoItemFoundException.class)
                                        
                        // Model
                        .addPackages(true, "com.alekseyorlov.luna.domain")
                        .addPackages(true, "com.alekseyorlov.luna.ejb.domain")
                        
                        // Liquibase loader
                        .addPackages(true, "com.alekseyorlov.luna.liquibase")
                        .addAsResource("liquibase")
                        
                        // JPA descriptor
                        .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
                        
                        // CDI descriptor
                        .addAsManifestResource("META-INF/test-beans-liquibase-mocked-data.xml", "beans.xml")
                
                        // EJB descriptor
                        .addAsManifestResource("META-INF/ejb-jar.xml", "ejb-jar.xml"))
                .addAsModule(create(WebArchive.class, "webapp.war")
                        
                        // This test
                        .addClass(EntryEJBTest.class))
                .addAsLibraries(resolver().loadPomFromFile("pom.xml")
                        
                        // 3rd party dependencies
                        .resolve(
                                "com.h2database:h2",
                                "org.liquibase:liquibase-core",
                                "com.github.slugify:slugify")
                        .withTransitivity()
                        .asList(JavaArchive.class));
    }
    
    @PersistenceContext
    EntityManager em;
     
    @Inject
    UserTransaction utx;
    
    @EJB
    private EntryEJB entryService;

    @Before
    public void preparePersistenceTest() throws Exception {
        utx.begin();
        em.joinTransaction();
    }
    
    @After
    public void commitTransaction() throws Exception {
        utx.rollback();
    }
    
    @Test
    public void shouldReadEntry() throws NoItemFoundException {
        
        // given
        final long entryId = 1;
        
        // when
        final Entry entry = entryService.find(entryId);
        
        // then
        assertNotNull(entry);
        assertEquals(entryId, entry.getId().longValue());
    }
    
    @Test
    public void shoudlReadAllEntries() {
        
        // given
        final int page = 1; 
        final Pageable pageable = new Pageable(page); 
        
        // when
        final Page<Entry> entryPage = entryService.findAll(pageable);
        
        // then
        assertNotNull(entryPage);
        assertEquals(2, entryPage.getTotalElements().intValue());
        assertEquals(1, entryPage.getTotalPages().intValue());
        assertEquals(2, entryPage.getContent().size());
    }
    
    @Test
    public void shouldCreateEntry() {
        
        // given
        final User user = em.find(User.class, 1L);
        final EntryType entryType = em.find(EntryType.class, "raw");
        final ElementType elementType = em.find(ElementType.class, "markdown");
        
        // entry element
        final Element element = new Element();
        element.setType(elementType);
        
        Map<String, String> entryElementData = new HashMap<String, String>();
        entryElementData.put("markup", "Some markup.");
        element.setData(entryElementData);
        
        // entry
        Entry entry = new Entry();
        entry.setOwner(user);
        entry.setStatus(Status.NOT_PUBLISHED);
        entry.setTitle("whatever title");
        entry.setType(entryType);
        entry.setElements(Arrays.asList(element));
        entry.setCreatedAt(Instant.now());
        
        // when
        entryService.save(entry);
        
        // then
        assertNotNull(entry.getId());
    }
    
    @Test
    public void shouldDeleteEntry() throws NoItemFoundException {
        
        // given
        final long entryId = 1;
        final Entry entry = entryService.find(entryId);
        
        // when
        entryService.delete(entry);
        em.flush(); // because em is in joined transaction state
        
        // then throw NoResultException
        assertNull(em.find(Entry.class, entryId));
    }
    
    @Test
    public void shouldUpdateEntry() throws NoItemFoundException {
        
        // given
        final long entryId = 1;
        final Entry entry = entryService.find(entryId);
        
        entry.setTitle("new title");
        
        // when
        entryService.update(entry);
        em.flush(); // because em is in joined transaction state
        
        // then
        em.clear();

        assertEquals("new title", entryService.find(entryId).getTitle());
    }
}
