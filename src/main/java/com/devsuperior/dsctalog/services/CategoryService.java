package com.devsuperior.dsctalog.services;

import com.devsuperior.dsctalog.entities.Category;
import com.devsuperior.dsctalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll()
    {
        return categoryRepository.findAll();
    }
}
