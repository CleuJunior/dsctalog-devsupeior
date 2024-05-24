package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(cat -> mapper.map(cat, CategoryDTO.class));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       var optionalCategory = categoryRepository.findById(id);
       var response = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

       return mapper.map(response, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO request) {
        var response = mapper.map(request, Category.class);
        response = categoryRepository.save(response);

        return mapper.map(response, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO request) {
        try {
            Category response = categoryRepository.getOne(id);
            response.setName(request.getName());
            response = categoryRepository.save(response);

            return mapper.map(response, CategoryDTO.class);

        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);

        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

}
