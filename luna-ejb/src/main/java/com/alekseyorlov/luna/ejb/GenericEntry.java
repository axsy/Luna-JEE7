package com.alekseyorlov.luna.ejb;

import com.alekseyorlov.luna.ejb.domain.Page;
import com.alekseyorlov.luna.ejb.domain.Pageable;
import com.alekseyorlov.luna.ejb.exception.NoItemFoundException;

public interface GenericEntry<Entry, IdType> {

	Entry find(IdType id) throws NoItemFoundException ;
	
	Page<Entry> findAll(Pageable pageable);
	
	Entry save(Entry entry);
	
	Entry update(Entry entry);
	
	void delete(Entry id);
	
}
