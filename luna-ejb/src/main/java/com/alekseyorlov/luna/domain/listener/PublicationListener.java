package com.alekseyorlov.luna.domain.listener;

import com.alekseyorlov.luna.domain.Entry;
import com.alekseyorlov.luna.domain.Entry.Status;

import java.time.Instant;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PublicationListener {

    @PreUpdate
    @PrePersist
    public void publicationAudit(Entry entry) {
        Status previousStatus = entry.getPreviousStatus();

        // Published
        if ((previousStatus != null && previousStatus != Status.PUBLISHED && entry.getStatus() == Status.PUBLISHED)
        		|| (previousStatus == null && entry.getStatus() == Status.PUBLISHED)) {
            entry.setPublishedAt(Instant.now());
        }

        // Unpublished
        if (previousStatus != null && previousStatus == Status.PUBLISHED && entry.getStatus() != Status.PUBLISHED) {
            entry.setPublishedAt(null);
        	entry.setUnpublishedAt(Instant.now());
        }
    }

    @PostLoad
    @PostUpdate
    @PostPersist
    public void savePreviousStatus(Entry entry) {
        entry.setPreviousStatus(entry.getStatus());
    }

}
