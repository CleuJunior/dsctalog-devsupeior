package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        Page<Category> list = this.categoryRepository.findAll(pageable);
        return list.map(CategoryDTO::new);

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> obj = this.categoryRepository.findById(id);
       Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
       return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = this.categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = this.categoryRepository.getOne(id);
            entity.setName(dto.getName());
            entity = this.categoryRepository.save(entity);
            return new CategoryDTO(entity);

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
