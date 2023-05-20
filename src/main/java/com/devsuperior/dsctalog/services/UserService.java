package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.RoleDTO;
import com.devsuperior.dsctalog.dto.UserDTO;
import com.devsuperior.dsctalog.dto.UserInsertDTO;
import com.devsuperior.dsctalog.dto.UserUpdateDTO;
import com.devsuperior.dsctalog.entities.Role;
import com.devsuperior.dsctalog.entities.User;
import com.devsuperior.dsctalog.repositories.RoleRepository;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        return this.userRepository.findAll(pageable)
                .map(user -> this.mapper.map(user, UserDTO.class));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
       Optional<User> optionalUser = this.userRepository.findById(id);
       User response = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

       return this.mapper.map(response, UserDTO.class);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO request) {
        User entity = new User();
        copyDtoToEntity(request, entity);
        entity.setPassword(this.passwordEncoder.encode(request.getPassword()));
        entity = this.userRepository.save(entity);

        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDto) {

        try {
            User entity = this.userRepository.getOne(id);
            copyDtoToEntity(userUpdateDto, entity);
            entity = this.userRepository.save(entity);
            return new UserDTO(entity);

        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            this.userRepository.deleteById(id);
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
            Role role = this.roleRepository.getOne(roleDto.getId());
            userEntity.getRoles().add(role);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);

        if (user == null) {
            logger.error("User not found: " + username);
            throw new UsernameNotFoundException("Email not found");
        }

        logger.info("User found: " + username);
        return user;
    }

}
