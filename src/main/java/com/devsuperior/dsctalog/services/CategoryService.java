package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.dto.CategoryDTO;
import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import com.devsuperior.dsctalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll()
    {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> obj = categoryRepository.findById(id);
       Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
       return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }
}
