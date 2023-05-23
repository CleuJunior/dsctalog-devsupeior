package com.devsuperior.dsctalog.dto;

import com.devsuperior.dsctalog.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    
    @Email(message = "Entrar com um email válido")
    private String email;

    @Setter(AccessLevel.NONE)
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User entityUser){
        this.id = entityUser.getId();
        this.firstName = entityUser.getFirstName();
        this.lastName = entityUser.getLastName();
        this.email = entityUser.getEmail();
        entityUser.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }
}
