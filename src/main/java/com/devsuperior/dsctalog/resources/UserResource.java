package com.devsuperior.dsctalog.resources;

import com.devsuperior.dsctalog.dto.UserDTO;
import com.devsuperior.dsctalog.dto.UserInsertDTO;
import com.devsuperior.dsctalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
        Page<UserDTO> list = userService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findByID(@PathVariable Long id)
    {
        UserDTO dto = userService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO dto)
    {
        UserDTO userDTO = userService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto)
    {
        dto = userService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id)
    {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
