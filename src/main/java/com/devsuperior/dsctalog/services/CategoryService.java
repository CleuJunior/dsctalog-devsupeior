package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.services.exceptions.DatabaseException;
import com.devsuperior.dsctalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest)
    {
        Page<Category> list = categoryRepository.findAll(pageRequest);
        return list.map(x -> new CategoryDTO(x));

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> obj = categoryRepository.findById(id);
       Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
       return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {

        try {
            Category entity = categoryRepository.getOne(id);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);

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
