package com.devsuperior.dsctalog.entities;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public Category()
    {

    }

    public Category(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId().equals(category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
