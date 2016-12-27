package com.alekseyorlov.luna.ejb;

import javax.ejb.Local;

import com.alekseyorlov.luna.domain.Entry;

@Local
public interface DomainEntry extends GenericEntry<Entry, Long> {
}
