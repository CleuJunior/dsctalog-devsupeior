package com.devsuperior.dsctalog.services;



import com.devsuperior.dsctalog.dto.RoleDTO;
import com.devsuperior.dsctalog.dto.UserDTO;
import com.devsuperior.dsctalog.entities.Role;
import com.devsuperior.dsctalog.entities.User;
import com.devsuperior.dsctalog.repositories.RoleRepository;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable)
    {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
       Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
       return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserDTO userDTO) {
        User entity = new User();
        copyDtoToEntity(userDTO, entity);
        entity = userRepository.save(entity);

        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {

        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(userDTO, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);

        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }


    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);

        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User userEntity) {
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());

        userEntity.getRoles().clear();

        for (RoleDTO roleDto: userDTO.getRoles()) {
            Role role = roleRepository.getOne(roleDto.getId());
            userEntity.getRoles().add(role);
        }
    }

}
