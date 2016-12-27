package com.alekseyorlov.luna.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "taxonomy_types")
public class TaxonomyType implements Serializable {

	private static final long serialVersionUID = -1444094674967377424L;

	@Id
    private String id;

    @NotNull
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
