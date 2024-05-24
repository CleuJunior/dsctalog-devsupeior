package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.UserDTO;
import com.devsuperior.dsctalog.dto.UserInsertDTO;
import com.devsuperior.dsctalog.dto.UserUpdateDTO;
import com.devsuperior.dsctalog.entities.User;
import com.devsuperior.dsctalog.repositories.RoleRepository;
import com.devsuperior.dsctalog.repositories.UserRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> mapper.map(user, UserDTO.class));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        var optionalUser = userRepository.findById(id);
        var response = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return mapper.map(response, UserDTO.class);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO request) {
        var entity = new User();

        copyDtoToEntity(request, entity);

        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity = userRepository.save(entity);

        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDto) {

        try {
            var entity = userRepository.getOne(id);
            copyDtoToEntity(userUpdateDto, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User userEntity) {
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());

        userEntity.getRoles().clear();

        userDTO.getRoles().forEach(roleDto -> {
            var role = roleRepository.getOne(roleDto.getId());
            userEntity.getRoles().add(role);

        });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);

        if (isNull(user)) {
            log.error("User not found: " + username);
            throw new UsernameNotFoundException("Email not found");
        }

        log.info("User found: " + username);
        return user;
    }

}
