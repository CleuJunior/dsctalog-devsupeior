package com.devsuperior.dsctalog.entities;

import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;


    public Role() { }

    public Role(Long id, String authority){
        this.id = id;
        this.authority = authority;
    }

    public String getAuthority() { return authority; }

    public void setAuthority(String authority) { this.authority = authority; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
