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
        return this.categoryRepository.findAll(pageable)
                .map(cat -> this.mapper.map(cat, CategoryDTO.class));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> optionalCategory = this.categoryRepository.findById(id);
       Category response = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

       return this.mapper.map(response, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO request) {
        Category response = this.mapper.map(request, Category.class);
        response = this.categoryRepository.save(response);

        return this.mapper.map(response, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO request) {
        try {
            Category response = this.categoryRepository.getOne(id);
            response.setName(request.getName());
            response = this.categoryRepository.save(response);

            return this.mapper.map(response, CategoryDTO.class);

        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            this.categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);

        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

}
