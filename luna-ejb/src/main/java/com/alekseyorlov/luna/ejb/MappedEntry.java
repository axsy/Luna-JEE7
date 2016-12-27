package com.alekseyorlov.luna.ejb;

import javax.ejb.Local;

import com.alekseyorlov.luna.dto.Entry;

@Local
public interface MappedEntry extends GenericEntry<Entry, Long> {
}
