package com.alekseyorlov.luna.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.alekseyorlov.luna.domain.listener.AuditingListener;
import com.alekseyorlov.luna.domain.listener.PublicationListener;
import com.alekseyorlov.luna.domain.listener.SlugListener;
import com.alekseyorlov.luna.domain.listener.annotation.CreatedAt;
import com.alekseyorlov.luna.domain.listener.annotation.CreatedBy;
import com.alekseyorlov.luna.domain.listener.annotation.Slug;
import com.alekseyorlov.luna.domain.listener.annotation.UpdatedAt;

@Entity
@Table(name = "entries")
@EntityListeners({
        AuditingListener.class,
        SlugListener.class,
        PublicationListener.class
})
@NamedQueries({
		@NamedQuery(name = Entry.NAMED_FIND_QUERY,
					query = "SELECT e FROM Entry e WHERE e.id = :id"),
		@NamedQuery(name = Entry.NAMED_FIND_ALL_DESC_QUERY,
					query = "SELECT e FROM Entry e ORDER BY e.id DESC"),
		@NamedQuery(name = Entry.NAMED_COUNT_QUERY,
					query = "SELECT COUNT(e.id) FROM Entry e")
		
})
public class Entry implements Serializable {

	private static final long serialVersionUID = 522343525407350725L;

	public static final String NAMED_FIND_QUERY = "findQuery";
	public static final String NAMED_FIND_ALL_DESC_QUERY = "findAllDescValuePagedQuery";
	public static final String NAMED_COUNT_QUERY = "countQuery";
	
	public enum Status {
        PUBLISHED,
        NOT_PUBLISHED,
        DRAFT,
        TIMED_PUBLISH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(optional = false)
    @CreatedBy
    private User owner;

    @NotNull
    private Status status;

    @NotNull
    private String title;

    @NotNull
    @Slug(source = "title")
    private String slug;

    @ManyToOne(optional = false)
    private EntryType type;

    @NotEmpty
    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderColumn(name = "order_id")
    private List<Element> elements;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "entries_taxonomies",
            joinColumns = @JoinColumn(name = "entry_id", referencedColumnName = "id"),
            inverseJoinColumns= @JoinColumn(name = "taxonomy_id", referencedColumnName = "id")
    )
    private List<Taxonomy> taxonomies;

    @CreatedAt
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdatedAt
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "unpublished_at")
    private Instant unpublishedAt;

    @Transient
    private Status previousStatus;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        for(Element element: elements) {
            element.setEntry(this);
        }
        this.elements = elements;
    }

    public List<Taxonomy> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(List<Taxonomy> taxonomies) {
        this.taxonomies = taxonomies;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getUnpublishedAt() {
        return unpublishedAt;
    }

    public void setUnpublishedAt(Instant unpublishedAt) {
        this.unpublishedAt = unpublishedAt;
    }

	public Status getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(Status previousStatus) {
		this.previousStatus = previousStatus;
	}

}
