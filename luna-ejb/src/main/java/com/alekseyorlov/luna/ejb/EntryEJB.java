package com.alekseyorlov.luna.ejb;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.ejb.domain.Page;
import com.alekseyorlov.luna.ejb.domain.Pageable;
import com.alekseyorlov.luna.ejb.exception.NoItemFoundException;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EntryEJB implements DomainEntry {
    
    @PersistenceContext
	private EntityManager em;
	
    @Resource(name = "entryPageSize")
	private Integer pageSize;
	
	@Override
	public Entry find(Long id) throws NoItemFoundException {
		TypedQuery<Entry> entryQuery = em.createNamedQuery(Entry.NAMED_FIND_QUERY, Entry.class);
		entryQuery.setParameter("id", id);
		
		try {
		    return entryQuery.getSingleResult();
		} catch (NoResultException e) {
		    throw new NoItemFoundException(e);
		}
	}

	@Override
	public Page<Entry> findAll(Pageable pageable) {
		
		// Get total count of entries
		long entriesCount = ((Number)em.createNamedQuery(Entry.NAMED_COUNT_QUERY).getSingleResult()).longValue();
		
		// Get entries page
		TypedQuery<Entry> entryQuery = em.createNamedQuery(Entry.NAMED_FIND_ALL_DESC_QUERY, Entry.class);
		entryQuery.setFirstResult((pageable.getPageNumber() - 1) * pageSize);
		entryQuery.setMaxResults(pageSize);
		
		return new Page<Entry>(entryQuery.getResultList(), entriesCount, (long) Math.ceil(
		        (double) entriesCount / pageSize));
	}

	@Override
	public Entry save(Entry entry) {
		em.persist(entry);
		
		return entry;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Entry update(Entry entry) {
		
		return em.merge(entry);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(Entry entry) {
		em.remove(em.merge(entry));
	}

}
