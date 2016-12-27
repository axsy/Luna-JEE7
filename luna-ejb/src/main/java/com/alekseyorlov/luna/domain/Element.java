package com.alekseyorlov.luna.domain;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "elements")
public class Element implements Serializable {

	private static final long serialVersionUID = 8780372323770135017L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(optional = false)
    private ElementType type;

    @ManyToOne(optional = false)
    private Entry entry;

    @ElementCollection(fetch = EAGER)
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "elements_data")
    @Lob
    private Map<String, String> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!getType().equals(element.getType())) return false;
        return getData() != null ? getData().equals(element.getData()) : element.getData() == null;

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }
}
