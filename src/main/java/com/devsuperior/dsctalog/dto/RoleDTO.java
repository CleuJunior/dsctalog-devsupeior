package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.entities.Role;

import java.io.Serializable;

public class RoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;

    public RoleDTO(){ }

    public RoleDTO(Long id, String authority){
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role role){
        this.id = role.getId();
        this.authority = role.getAuthority();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getAuthority() { return authority; }

    public void setAuthority(String authority) { this.authority = authority; }
}
