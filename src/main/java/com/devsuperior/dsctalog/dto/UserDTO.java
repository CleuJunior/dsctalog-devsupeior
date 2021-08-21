package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.entities.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    
    @Email(message = "Entrar com um email válido")
    private String email;

    Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(){ }

    public UserDTO(User entityUser){
        this.id = entityUser.getId();
        this.firstName = entityUser.getFirstName();
        this.lastName = entityUser.getLastName();
        this.email = entityUser.getEmail();
        entityUser.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;  }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Set<RoleDTO> getRoles() { return roles; }
}
